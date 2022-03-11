# Copyright © 2021 Teamapps.org
# Licensed under the Apache License, Version 2.0
"""
Generates Entries for EmojiIcon Library or CSV with emoji data
"""
import argparse
import io
import os
import sys
import unicodedata
import re

REG_IND_LETTR = ('1F1E6 1F1E7 1F1E8 1F1E9 1F1EA 1F1EB 1F1EC 1F1ED '
                 '1F1EE 1F1EF 1F1F0 1F1F1 1F1F2 1F1F3 1F1F4 1F1F5 '
                 '1F1F6 1F1F7 1F1F8 1F1F9 1F1FA 1F1FB 1F1FC 1F1FD '
                 '1F1FE 1F1FF').split()

TAG_LAT_LETTR = ('E0061 E0062 E0063 E0064 E0065 E0066 E0067 E0068 E0069 E006A '
                 'E006B E006C E006D E006E E006F E0070 E0071 E0072 E0073 E0074 '
                 'E0075 E0076 E0077 E0078 E0079 E007A').split()

SKIP_STATUSES = ('unqualified', 'non-fully-qualified', 'minimally-qualified')

FILE_PREFIX = 'u'

WORKING_DIR = os.path.dirname(os.path.abspath(__file__))
TEST_INPUT_PATH = os.path.join(WORKING_DIR, 'emoji-test-13.1.txt')


def strip_accents(s):
    return ''.join(c for c in unicodedata.normalize('NFD', s)
                    if unicodedata.category(c) != 'Mn')


def append_to_file(fpath, data, enc='utf-8'):
    with io.open(fpath, 'a', encoding=enc) as fp:
        fp.write(data)

def init_file(fpath, data, enc='utf-8'):
    with io.open(fpath, 'w', encoding=enc) as fp:
        fp.write(data)

def parse_emoji_test_file(filename):
    """
    Parses Unicode's 'emoji-test.txt' file (available from
    http://unicode.org/Public/emoji/M.m/ where 'M.m' is the version number)
    and returns a list of code points.
    """
    with io.open(filename, encoding='utf-8') as fp:
        lines = fp.read().splitlines()

    emoji_list = []
    for line in lines:
        line = line.strip()
        if not line or line.startswith('#'):
            continue
        codepoints, status_emoname = line.split(';')
        status = status_emoname.split('#', 1)[0].strip()
        emo_info = status_emoname.split('#', 1)[1].strip()
        emo_unicode = emo_info.split(' ')[0].strip()
        emo_version = emo_info.split(' ')[1].strip()
        emo_name = emo_info.split(emo_version)[1].strip()
        emo_data = [emo_unicode, emo_name, emo_version]
        # print('; '.join(emo_data))
        if status in SKIP_STATUSES:
            continue
        cdpts = codepoints.strip().split();
        emoji_list.append([cdpts, emo_data])
    return emoji_list


def emoji_data(emoji):
    cps = emoji[0]
    emoji_data = emoji[1]
    emoji_unicode =  emoji_data[0]
    emoji_name = emoji_data[1]
    # determine if it's a country/regional flag
    is_flag = False
    if len(cps) > 1 and cps[1] in (REG_IND_LETTR + TAG_LAT_LETTR):
        is_flag = True

    cps_html = ''.join('&#x{};'.format(cp) for cp in cps)

    # filenames have no 'FE0F' or 'E007F' components
    cps_filename = [cp for cp in cps if cp not in ('FE0F', 'E007F')]
    filename = FILE_PREFIX + '_'.join(cps_filename).lower()

    png_dir = 'flags_png' if is_flag else 'png'
    svg_dir = 'flags' if is_flag else 'svg'
    sbw_dir = 'flags_bw' if is_flag else 'svg_bw'

    png_file = "{}/{}.png".format(png_dir, filename)
    svg_file = "{}/{}.svg".format(svg_dir, filename)
    sbw_file = "{}/{}.svg".format(sbw_dir, filename)

    base_name = emoji_name.split(':', 1)[0].strip()
    try:
        variant = emoji_name.split(':', 1)[1].strip()
        safe_name = "{}__{}".format(base_name, variant)
    except IndexError:
        variant = ''
        safe_name = base_name



    if is_flag:
        flag_name = emoji_name.split(':', 1)[1].strip()
        base_name = flag_name # for csv
        safe_country = strip_accents(flag_name)
        safe_name = "flag_{}".format(safe_country)
        variant = ''

    safe_name = re.sub(r' ', '_', safe_name) # space to underline
    safe_name = re.sub(r'#', 'hash', safe_name) # keycap #
    safe_name = re.sub(r'[*]', 'star', safe_name) # keycap *
    safe_name = re.sub(r'keycap__', 'keycap_', safe_name) # keycap__
    safe_name = re.sub(r'ñ', 'n', safe_name) # piñata
    safe_name = re.sub(r'^1st(.*)', r'first\1', safe_name) # beginning with number
    safe_name = re.sub(r'^2nd(.*)', r'second\1', safe_name) # beginning with number
    safe_name = re.sub(r'^3rd(.*)', r'third\1', safe_name) # beginning with number
    safe_name = re.sub(r'[\W+]', '', safe_name).lower() # remove all non-word characters

    # return [i, emoji_unicode, safe_name, emoji_name, base_name, variant, '_'.join(cps), cps_html, filename, png_file, svg_file, sbw_file]
    return {
        'emoji_unicode': emoji_unicode,
        'safe_name': safe_name,
        'emoji_name': emoji_name,
        'base_name': base_name,
        'variant': variant,
        'cps': cps,
        'cps_html': cps_html,
        'filename': filename,
        'png_file': png_file,
        'svg_file': svg_file,
        'sbw_file': sbw_file,
        'is_flag' : is_flag
    }


def write_csv(emoji_list):
    csv_file = os.path.join(WORKING_DIR, '..', 'emoji.csv')
    # csv_header = '\t'.join(["emoji_unicode", "emoji_name", "codepoints_", "cps_html", "png_file", "svg_file", "sbw_file"])+"\n"
    csv_header = '"number", "emoji_unicode", "safe_name", "emoji_name", "base_name", "variant",  "codepoints_", "cps_html", "png_file", "svg_file", "sbw_file" \n'
    init_file(csv_file, csv_header)
    for i, emoji in enumerate(emoji_list, 1):
        emo_data = emoji_data(emoji)
        #csv_line = '"{}", "{}", "{}", "{}", "{}", "{}", "{}", "{}", "{}", "{}", "{}" \n'.format(i, emoji_unicode, safe_name, emoji_name, base_name, variant, '_'.join(cps), cps_html, png_file, svg_file, sbw_file)
        csv_line = '"{}", "{}", "{}", "{}", "{}", "{}", "{}", "{}", "{}", "{}", "{}" \n'.format(i, emo_data['emoji_unicode'], emo_data['safe_name'], emo_data['emoji_name'], emo_data['base_name'], emo_data['variant'], '_'.join(emo_data['cps']), emo_data['cps_html'], emo_data['filename'], emo_data['png_file'], emo_data['svg_file'], emo_data['sbw_file'])
        # csv_line = emo_data["safe_name"]
        append_to_file(csv_file, csv_line)

def write_java(emoji_list):
    java_file = os.path.join(WORKING_DIR, 'emoji.java')
    java_header = '        // generated entries \n'
    init_file(java_file, java_header)
    for i, emoji in enumerate(emoji_list, 1):
        emo_data = emoji_data(emoji)
        #csv_line = '"{}", "{}", "{}", "{}", "{}", "{}", "{}", "{}", "{}", "{}", "{}" \n'.format(i, emoji_unicode, safe_name, emoji_name, base_name, variant, '_'.join(cps), cps_html, png_file, svg_file, sbw_file)
        # csv_line = '"{}", "{}", "{}", "{}", "{}", "{}", "{}", "{}", "{}", "{}", "{}" \n'.format(i, emo_data['emoji_unicode'], emo_data['safe_name'], emo_data['emoji_name'], emo_data['base_name'], emo_data['variant'], '_'.join(emo_data['cps']), emo_data['cps_html'], emo_data['filename'], emo_data['png_file'], emo_data['svg_file'], emo_data['sbw_file'])
        # csv_line = emo_data["safe_name"]
        # append_to_file(csv_file, csv_line)
        # java_line = 'public static final EmojiIcon {} = create("{}", "{}", "{}", "{}", "{}", "{}", "{}", "{}", "{}", "{}", "{}");\n'.format(safe_name.upper(), i, emoji_unicode, safe_name, emoji_name, base_name, variant, '_'.join(cps), cps_html, png_file, svg_file, sbw_file)
        is_flag_java_boolean = "true" if emo_data['is_flag'] else "false"
        java_line = '        public static final EmojiIcon {} = create({}, "{}", "{}", {});\n'.format(emo_data['safe_name'].upper(), i, emo_data['emoji_unicode'], emo_data['safe_name'].upper(), is_flag_java_boolean)
        append_to_file(java_file, java_line)

def main(args=None):
    parser = argparse.ArgumentParser(description=__doc__)

    parser.add_argument(
        '-C',
        '--csv',
        help="generates csv instead of html",
        action='store_true',
    )
    parser.add_argument(
        '-J',
        '--java',
        help="generates java",
        action='store_true',
    )
    opts = parser.parse_args(args)

    emoji_list = parse_emoji_test_file(TEST_INPUT_PATH)

    if opts.csv:
        write_csv(emoji_list)
    elif opts.java:
        write_java(emoji_list)
    else:
        write_java(emoji_list)

if __name__ == "__main__":
    sys.exit(main())

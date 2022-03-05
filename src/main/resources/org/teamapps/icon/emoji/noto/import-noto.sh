#!/bin/bash

###
# ========================LICENSE_START=================================
# TeamApps Emoji Icon Library
# ---
# Copyright (C) 2021 - 2022 TeamApps.org
# ---
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
# 
#      http://www.apache.org/licenses/LICENSE-2.0
# 
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
# =========================LICENSE_END==================================
###


# import svg files from google noto-emoji project
# Author: Philipp Gassmann
# Usage: ./import-noto.sh path/to/google/noto-emoji

# TODO: Currently using branch svg_flags2. check if merged to main branch.

[ -z "$1" ] && echo "Usage: ./import-noto.sh path/to/google/noto-emoji" && exit 1

set -eux
set -o pipefail
dir="$(dirname $(readlink -f $0))"
cd $dir

source_dir="$1"

rm -rf svg/
rm -rf region-flags/waved-svg/

rsync -a "${source_dir}/svg/" svg/
rsync -a "${source_dir}/third_party/region-flags/waved-svg/" region-flags/waved-svg/
python3 svg_cleaner.py svg
python3 svg_cleaner.py region-flags/waved-svg


## fixes

# remove suffix from some flags
mv region-flags/waved-svg/emoji_u1f3f4_e0067_e0062_e0065_e006e_e0067{_e007f,}.svg
mv region-flags/waved-svg/emoji_u1f3f4_e0067_e0062_e0073_e0063_e0074{_e007f,}.svg
mv region-flags/waved-svg/emoji_u1f3f4_e0067_e0062_e0077_e006c_e0073{_e007f,}.svg

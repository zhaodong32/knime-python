#!/bin/bash

set -x
cd ~/python
if [[ ! -d ~/anaconda3 ]]; then
        if [[ "$(uname -s)" == "Darwin" ]]; then
                # INST_FILE=Miniconda3-4.4.10-MacOSX-x86_64.sh
                INST_FILE=Anaconda3-5.1.0-MacOSX-x86_64.sh
        else
                # INST_FILE=Miniconda3-4.4.10-Linux-x86_64.sh
                INST_FILE=Anaconda3-5.1.0-Linux-x86_64.sh
        fi

        # wget -qN https://repo.anaconda.com/miniconda/$INST_FILE
        wget -qN https://repo.anaconda.com/archive/$INST_FILE
        bash $INST_FILE -b -p ~/anaconda3
        rm $INST_FILE
fi

export PATH=~/anaconda3/bin:$PATH

conda env remove -n py3_knime -q -y || true
conda env remove -n py2_knime -q -y || true

conda env create -f py3_knime.yml -q
conda env create -f py2_knime.yml -q

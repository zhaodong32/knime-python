#!/bin/bash -e

KNIME_PATH=${KNIME_PATH:-}

# TODO only add debug flags if they are not already there
if [[ -n "${KNIME_PATH}" ]]; then
  echo "====>>> Adding debug flags to knime.ini <<<===="
  cat >> "${KNIME_PATH}/knime.ini" <<EOF
-Xdebug
-Xrunjdwp:transport=dt_socket,address=8998,server=y,suspend=n
EOF

else
  echo "====>>> Info: No KNIME Executor path was specified. Cannot proceed with the installation without one. <<<===="
  exit
fi

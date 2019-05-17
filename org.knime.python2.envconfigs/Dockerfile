# Use an official Python runtime as a parent image
FROM ubuntu:18.04

# Set the working directory to /python
WORKDIR /root/python

# Install requirements
RUN apt-get update && apt-get -y upgrade
RUN apt-get install -y wget sudo

# Copy the installer scripts to the container
COPY buildscripts/install-anaconda.sh /root/python/
COPY scripts/installers/knime_ap_executor/knime-ap-exec_install.sh /root/python/
COPY scripts/installers/knime_extensions/knime-ext_install.sh /root/python/

# Install knime nightly
ENV KNIME_USER 'knime'
ENV KNIME_VERSION '3.8.0_latest'
ENV KNIME_FILE 'knime_latest.linux.gtk.x86_64.tar.gz'
ENV KNIME_DOWNLOAD_URL 'http://download.knime.org/analytics-platform/nightly/linux/x86_64/'
run chmod +x knime-ap-exec_install.sh
run ./knime-ap-exec_install.sh

# Install the python extension
ENV KNIME_UPDATE_SITES 'http://update.knime.com/analytics-platform/3.8',
ENV KNIME_FEATURES 'org.knime.features.python2.feature.group,org.knime.features.testingapplication.feature.group',
ENV KNIME_PATH '/home/knime/Applications/knime-3.8.0_latest'
run chmod +x knime-ext_install.sh
run ./knime-ext_install.sh

# Copy environment files and configuration
RUN chown knime:knime /home/knime
RUN chmod 755 /home/knime
RUN sudo -u knime mkdir -p /home/knime/python
COPY knime-preferences.eps /home/knime/python/
COPY envconfigs/linux/test/py2_knime.yml /home/knime/python/
COPY envconfigs/linux/test/py3_knime.yml /home/knime/python/

# Install conda
# FIXME: THIS FAILS: SOMETHING WITH THE USERS IS WRONG
# NotWritableError: The current user does not have write permissions to a required path.
#   path: /home/knime/.conda/pkgs/urls.txt
#   uid: 1000
#   gid: 1000
RUN sudo -H -u knime ./install-anaconda.sh

# CMD ["/home/knime/Applications/knime-3.8.0_latest/knime", \
#      "-noSplash", "-consoleLog", "--launcher.suppressErrors", \
#      "-application", "org.knime.testing.NGUnittestRunner", \
#      "-data", "foo", \
#      "-preferences", "/root/python/knime-preferences.eps", \
#      "-xmlResultDir", "/root/result/" \
#      "-outputToSeparateFile", \
#      "-include", "'^org\.knime\.core\.data\.util\.memory\..+$'" ]
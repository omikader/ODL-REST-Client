#!/bin/bash

# This script will satisfy all prerequisite conditions required to build and configure an OpenDaylight Controller and its DLUX UI on a Debian based Linux system
# Note: sudo must be installed prior to running this script

echo -e 'In order to install the OpenDaylight Controller, the following dependency prerequisite(s) must be satisifed:'
echo '1. A Java 7- or Java 8-compliant JDK'
echo '2. Properly set $JAVA_HOME'
echo '3. Maven 3.1.1 or later'

echo -e '\nIn order to run the DLUX UI, the following dependency prerequisite(s) must be satisfied:'
echo '4. NodeJS/npm'
sleep 5s

echo -e '\nPerforming system update and installing essential programs/pacakges (GNU C/C++ compiler and curl)' 
sleep 2s
sudo apt-get update
sudo apt-get upgrade
sudo apt-get install build-essential
sudo apt-get install curl

echo -e '\nInstalling a compliant JDK (1.8.71) from source'
echo 'In order to pull the proper tar.gz format, --header is required'
sleep 2s
cd /opt
sudo wget http://download.oracle.com/otn-pub/java/jdk/8u71-b15/jdk-8u71-linux-x64.tar.gz --no-cookies --header "Cookie: oraclelicense=accept-securebackup-cookie"

echo -e '\nExtracting the jdk into usr/lib/jvm.'
sleep 2s
sudo mkdir -p /usr/lib/jvm
sudo tar -zxvf jdk-8u71-linux-x64.tar.gz -C /usr/lib/jvm

echo -e '\nAssigning the newly downloaded JDK a higher priority than any existing JDK'
echo 'Typically, default installations are given a priorty of 1071'
sleep 2s
sudo update-alternatives --install "/usr/bin/java" "java" "/usr/lib/jvm/jdk1.8.0_71/bin/java" 2000
sudo update-alternatives --install "/usr/bin/javac" "javac" "/usr/lib/jvm/jdk1.8.0_71/bin/javac" 2000
sudo update-alternatives --install "/usr/bin/javaws" "javaws" "/usr/lib/jvm/jdk1.8.0_71/bin/javaws" 2000

echo -e '\nCorrecting file ownership of java, javac, and javaws'
sleep 2s
sudo chmod a+x /usr/bin/java
sudo chmod a+x /usr/bin/javac
sudo chmod a+x /usr/bin/javaws
sudo chown -R root:root /usr/lib/jvm/jdk1.8.0_71

echo -e '\nSelect newly installed java configuration via update-alternatives'
sleep 2s
sudo update-alternatives --config java
sudo update-alternatives --config javac
sudo update-alternatives --config javaws

echo -e '\nSettings %JAVA_HOME to the newly insalled jdk'
echo -e '.profile is executed automatically by the DisplayManager upon startup'
sleep 2s
sudo echo 'JAVA_HOME=/usr/lib/jvm/jdk1.8.0_71' >> ~/.profile
sudo echo 'export JAVA_HOME' >> ~/.profile
sudo echo 'PATH=$JAVA_HOME/bin:$PATH' >> ~/.profile
sudo echo 'export PATH' >> ~/.profile
. ~/.profile

echo -e '\nInstalling updated maven'
sleep 2s
sudo apt-get install maven

echo -e '\nPulling and installing nodejs'
sleep 2s
cd /opt
sudo wget https://nodejs.org/dist/v4.3.2/node-v4.3.2.tar.gz
sudo tar -zxvf node-v4.3.2.tar.gz
cd node-v4.3.2
sudo ./configure
sudo make
sudo make install
sudo npm install npm -g

echo -e '\nPulling and building OpenDaylight'
sleep 2s
cd /opt
sudo wget https://nexus.opendaylight.org/content/groups/public/org/opendaylight/integration/distribution-karaf/0.3.3-Lithium-SR3/distribution-karaf-0.3.3-Lithium-SR3.tar.gz
sudo tar -zxvf distribution-karaf-0.3.3-Lithium-SR3.tar.gz
cd distribution-karaf-0.3.3-Lithium-SR3

echo -e '\nInstalling OpenDaylight features, including DLUX'
sudo ./bin/karaf <<EOF
feature:install odl-restconf odl-l2switch-switch odl-mdsal-apidocs odl-dlux-core odl-dlux-node odl-dlux-yangui odl-dlux-all odl-ovsdb-all odl-openflowplugin-all odl-adsal-northbound odl-sdninterfaceapp-all odl-tsdr-all odl-adsal-all odl-aaa-authn odl-base-all odl-nsf-all  

EOF
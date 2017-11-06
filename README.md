Project Brooklyn -- Windows Instructions
=============================

This is the repository for Trove's main development effort.  As a fast way to get started, do the following:

1.  Navigate to https://bitbucket.org and sign in.

1.  Click on the TroveWeb repository, then on the panel on the left, click Downloads.

1.  Download TrovePrintSetup.zip and unpack it

1.  Download checkout.bat, leave it alone for now.

1.  Run GitHubSetup.exe to install GitHub

1.  Run jdk-7u71-windows-x64.exe.  Leave the default install directory where it is.

1.  Unzip apache-maven-3.2.5-bin

1.  Copy the directory within the unzipped apache-maven-3.2.5-bin (apache-maven-3.2.5) to “C:\Program Files\”

1.  Navigate to the Windows Environment Variable editor (Left click Start->Right click My Computer->Properties->Advanced System Settings->Environment Variables)

1.  Below the System Variables box, click the “New…” button.

1.  For the Variable Name, enter JAVA_HOME

1.  For the Variable Value, enter C:\Program Files\Java\jdk1.7.0_71

1.  In the same Systems Variable box, find the Path variable and click the “Edit…” button.

1.  At the end of the Variable Value string, add the following text: ;C:\Program Files\apache-maven-3.2.5\bin

1.  Run the shortcut on your desktop for “Git Shell”.

1.  Run the checkout.bat from within the shell window that opens.  For example: C:\Users\[YOUR_USER_NAME] \Downloads\checkout.bat, this will check the Trove code base out to you at C:\Users\workspace\Trove


Now you should be able to make edits to the project.  Once you’ve made edits, you can view your changes on a local server by performing the following:

1.  In the “Git Shell” command prompt, navigate to your Trove code base with the following command: cd C:\Users\workspace\Trove

1.  Run the following command: mvn appengine:devserver

1.  Open a browser window and go to the following URL: http://localhost:8888/

You can leave the server up and running and rebuild the code after making changes.  Open a second “Git Shell” command prompt and perform the following:

1.  In the “Git Shell” command prompt, navigate to your Trove code base with the following command: cd C:\Users\workspace\Trove

1.  Run the following command: mvn clean install

Once you're happy with your changes, you can commit them to bitbucket by performing the following:

1.  In the “Git Shell” command prompt, navigate to your Trove code base with the following command: cd C:\Users\workspace\Trove

1.  Run the following command: git commit -a

1.  Run the following command: git push


For convenience, I have converted the project over to use IntelliJ Ultimate edition.  This has a 30 day free trial, after which point you're welcome to start using Eclipse.
IntelliJ is a lot more workable, though, so I'll be purchasing my own personal copy for writing any code in the future.  To set up IntelliJ Ultimate,
perform the following:

1. Go to https://www.jetbrains.com/idea/download/ and download the IntelliJ Ultimate edition.  Install it.

1. Once it's been installed, run it.  The initial project loading box should show on the screen.  Click the "Open" button.

1. Navigate to C:\Users\workspace\Trove and click "Okay".  This will begin the load process.

1. Because Git was installed via GitHub, you'll have to show IntelliJ where to find the executable.  Click File->Settings->Expand Version Control->Git

1. Click the "..." button next to the "Path to Git Executable" box to navigate to where the Git executable is located.

1. Click the button at the top of the box explorer box that says "Show Hidden Files and Directories" (furthest button to the right).

1. Navigate to C:\Users\[YOUR_USER_NAME]\AppData\Local\GitHub\PortableGit_[a bunch of random numbers and letters]\bin\git.exe

1. Click okay to close the box.  The new path should show up in the settings.  Click Apply, Okay.

1. You should now be able to run and debug the project.  The "Play" button in the upper right hand corner of IntelliJ will start the AppEngine Dev server for you.

1. You can also run the various "Git" commands using a visual view by right clicking the brooklyn project in the project explorer->Git->Commit.


Linux Instructions
======

Install java

    # I've been using this, if you have issues you may need the oracle version
    # but that's more effort to install
    sudo apt-get install openjdk-7-jdk

Setup maven:

    mkdir ~/bin && cd ~/bin
    wget http://mirror.nexcess.net/apache/maven/maven-3/3.3.3/binaries/apache-maven-3.3.3-bin.tar.gz
    tar xzf apache-maven-3.3.3-bin.tar.gz
    cd apache-maven-3.3.3
    echo "export PATH=$PATH:$(pwd)/bin" >> ~/.bashrc
    source ~/.bashrc

This will assume you already have java7 installed (version is important), but you need to let
maven know how to find it.. Use `which java` and then use `ls -l <program-path>` to follow any
symbolic links. Once you've found the absolute path set the java home environment variable to
the full path of the jre directory:

    $ which java
    /usr/bin/java

    ## now follow any symbolic links
    $ ls -l /usr/bin/java
    /usr/bin/java -> /etc/alternatives/java*
    $ ls -l /etc/alternatives/java
    /etc/alternatives/java -> /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java*
    $ ls -l /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
    /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java*

    ## update JAVA_HOME
    echo "export JAVE_HOME=/usr/lib/jvm/java-7-openjdk-amd64/jre" >> ~/.bashrc
    source ~/.bashrc

Now install sql packages if necessary, the client package will also give you mysqldump

    sudo apt-get install mysql-client-5.5
    sudo apt-get install mysql-server-5.5

Copy database for dev site hosted on appengine:

    mysqldump -h173.194.106.210 -utrovelocaluser -p troveweb > /path/to/dump.sql

You will be prompted for the user password, this can be found in the dev config file
`src/main/java/com/troveup/config/properties/application-dev.switchme`. If you'd like to monitor
progress, open up a new terminal or tmux pane and watch the contents.

    tail -f /path/to/dump.sql

Next you should clear the troveweb database on your local mysql server

    mysql -u root # optionally pass the -p parameter if you added a password to your local mysql instance
    mysql> drop database troveweb;
    mysql> create database troveweb;
    mysql> exit

Now load the contents of the dev database dump into your local dev DB:

    mysql -u root troveweb < /path/to/dump.sql

Command to run server from repo root:

    mvn appengine:devserver


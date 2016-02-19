How to check in code using Netbeans

The instructions here assume you have checked out code using instructions here: NetbeansCheckoutCode

If you are creating a project for the first time and adding it to this svn repository the instructions are a little different (TODO add creating project instructions).

Make sure your changes compile before checking anything in.

Also make sure you don't check in .class or .jar files.  Netbeans by default allows them to be checked in.  You can make it ignore files/dirs like this:
  * click on 'Files' tab next to 'Projects' tab
  * drill down into Robot2012 folder and you see a list of folders:
    * build
    * j2meclasses
    * etc...
  * right click on a folder -> Subversion -> ignore. You should see the folder get grayed out.
  * repeat this for every folder/file you see except:
    * nbproject
    * resources
    * src
    * BSD\_License...
    * build.properties
    * build.xml

To commit changes:
  * right click on the top of the project name in 'Projects' window and select 'commit'.
  * you should see a list of the modified .java files and maybe some xml file.  If you see class/jar files click on checkbox to have them to have them ignored.
  * type in a message to explain what you are checking in.. and that should be it.
  * The username is your google id.
  * Password is not your google password. It is found here: http://code.google.com/p/frc2876/source/checkout -> click on googlecode.com password link
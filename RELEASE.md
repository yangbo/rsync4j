How to make a release
=====================

* Switch to Java 9
* Run the following command to deploy the artifact:

  ```
  mvn release:clean release:prepare release:perform
  ```

* Push all changes
* Edit tag generated by Maven 

  * Enter *release title*, eg: `Release v3.1.2-14`
  * Attach `-bin.zip` and `-spring-boot.jar` to the release 
    (from `rsync4j-all/target`)

* Update documentation

  * if necessary, install mkdocs in a virtual environment
    
    * `virtualenv -p /usr/bin/python3 venv`
    * `./venv/bin/pip install mkdocs==1.4.2 jinja2==3.1.2 "Markdown<3.4.0" mkdocs-material==8.5.10`
    
  * add new release link (`releases.md`)
  * update artifact version (`maven.md`)
  * update OpenSSH versions (`windows.md`)
  * test 
    
    ```
    ./venv/bin/mkdocsmkdocs build --clean && ./venv/bin/mkdocsmkdocs serve
    ```
    
  * deployment happens automatically on commit

ChangeLog
=====================

* 2024-10-15 [Bob](mailto:bo.yang@telecwin.com)

  * Change Java source and target level to Version 9 to support JDK9 module system.

  * Change rsync4j-core to JDK9 module named 'com.github.fracpete.rsync4j.core'.

  * Upgrade commons-lang to commons-lang3 version 3.17.0 to support JDK9 module system.
  
  * Upgrade commons-io to version 2.17.0 to avoid Vulnerabilities issue: Vulnerabilities from dependencies: [CVE-2024-47554](https://cve.mitre.org/cgi-bin/cvename.cgi?name=CVE-2024-47554).

  * Upgrade maven-javadoc-plugin to 3.10.1 to support javadoc of module-info.java.

  * Upgrade maven-compiler-plugin to 3.13.0 to support java9 module.

  * Upgrade processoutput4j to version 0.0.14-SNAPSHOT to support module.

  * Support windows7 64bits OS by using classic cygwin and rsync.

  * Add Utils.Windows API
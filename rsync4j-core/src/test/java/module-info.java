module com.github.fracpete.rsync4j.core.test {
    opens com.github.fracpete.rsync4j.core.test;

    requires org.apache.commons.lang3;
    requires org.apache.commons.io;
    requires java.logging;
    requires com.github.fracpete.processoutput4j;
    requires net.sourceforge.argparse4j;
    requires com.sun.jna;
    requires com.sun.jna.platform;
    requires org.junit.jupiter.api;
    requires com.github.fracpete.rsync4j.core;
}
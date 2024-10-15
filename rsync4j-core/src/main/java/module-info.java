module com.github.fracpete.rsync4j.core {
    exports com.github.fracpete.rsync4j;
    exports com.github.fracpete.rsync4j.core;

    requires org.apache.commons.lang3;
    requires org.apache.commons.io;
    requires java.logging;
    requires processoutput4j;
    requires net.sourceforge.argparse4j;
}
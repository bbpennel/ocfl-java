module edu.wisc.library.ocfl.aws {

    exports edu.wisc.library.ocfl.aws;

    requires edu.wisc.library.ocfl.api;
    requires edu.wisc.library.ocfl.core;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires org.apache.commons.codec;
    requires org.slf4j;
    requires software.amazon.awssdk.core;
    requires software.amazon.awssdk.services.s3;

}
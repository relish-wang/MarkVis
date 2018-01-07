package com.hustoj.bean;

/**
 * @author Relish Wang
 * @since 2018/01/07
 */

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

@Root(name = "script", strict = false)
public class LoginResult {

    @Attribute(name = "language")
    public String language;

    @Text
    public String script;
}

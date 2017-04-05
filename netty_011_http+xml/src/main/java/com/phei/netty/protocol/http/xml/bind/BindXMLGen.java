package com.phei.netty.protocol.http.xml.bind;

import java.io.IOException;

import org.jibx.binding.generator.BindGen;
import org.jibx.runtime.JiBXException;

public class BindXMLGen {
	
	public static void main(String[] args) throws JiBXException, IOException {
		
		BindGen.main(new String[]{"com.phei.netty.protocol.http.xml.pojo.Order"});
		
	}

}

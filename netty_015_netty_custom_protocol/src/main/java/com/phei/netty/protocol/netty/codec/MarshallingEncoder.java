/*
 * Copyright 2013-2018 Lilinfeng.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phei.netty.protocol.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;

import java.io.IOException;

import org.jboss.marshalling.Marshaller;

/**
 * @author Lilinfeng
 * @date 2014年3月14日
 * @version 1.0
 */
@Sharable
public class MarshallingEncoder {

    private static final byte[] LENGTH_PLACEHOLDER = new byte[4];
    Marshaller marshaller;

    public MarshallingEncoder() throws IOException {
	marshaller = MarshallingCodecFactory.buildMarshalling();
    }
    /**
     * 
     * @param msg 这里使用的是attachment中的value值
     * @param out
     * @throws Exception
     */
    protected void encode(Object msg, ByteBuf out) throws Exception {
	try {
	    int lengthPos = out.writerIndex();//Returns the writerIndex of this buffer.
	    out.writeBytes(LENGTH_PLACEHOLDER);
	    System.out.println(out.writerIndex());//lengthPos+4,writerIndex()相当于JDK中byteBuffer中的position()
	    ChannelBufferByteOutput output = new ChannelBufferByteOutput(out);//自定义的类实现了marshal的output接口
	    marshaller.start(output);//传入一个实现了output接口的对象，这个对象在构造是传入了out这个byteBuf
	    marshaller.writeObject(msg);//这里使用的是attachment中的value值,对msg进行序列化存储到out这个byteBuf中
	    marshaller.finish();//表示完成序列化
	    out.setInt(lengthPos, out.writerIndex() - lengthPos - 4);//将刚才LENGTH_PLACEHOLDER空字节数组占用的内容设置为序列化的value的字节大小
	} finally {
		//关闭与之相关的流，并不是关闭他本身
	    marshaller.close();//Closes the stream. This method must be called to release any resources associated with the stream
	}
    }
}

package com.stee.spfcore.webapi.messaging;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.SQLData;
import java.util.Date;

import com.stee.spfcore.webapi.model.AnnotatedModelList;
import com.stee.spfcore.webapi.model.system.Heartbeat;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.hibernate.converter.HibernatePersistentCollectionConverter;
import com.thoughtworks.xstream.hibernate.converter.HibernatePersistentMapConverter;
import com.thoughtworks.xstream.hibernate.converter.HibernatePersistentSortedMapConverter;
import com.thoughtworks.xstream.hibernate.converter.HibernatePersistentSortedSetConverter;
import com.thoughtworks.xstream.hibernate.converter.HibernateProxyConverter;
import com.thoughtworks.xstream.hibernate.mapper.HibernateMapper;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.mapper.MapperWrapper;
import com.thoughtworks.xstream.security.AnyTypePermission;
import com.thoughtworks.xstream.security.ExplicitTypePermission;
import com.thoughtworks.xstream.security.NoTypePermission;
import com.thoughtworks.xstream.security.WildcardTypePermission;

public class MessageStream {

    private XStream stream;

    public MessageStream() {
    	System.out.println("START message stream "+new Date());
        this.stream = new XStream( new StaxDriver() ) {
            protected MapperWrapper wrapMapper( final MapperWrapper next ) {
                return new HibernateMapper( next );
            }
        };
        
        //System.out.println("> START stream add "+new Date());
        Class<?>[] classes = new Class[] { 
        		String.class, 
        		byte[].class, 
        		java.sql.Date.class, 
        		java.sql.Timestamp.class, 
        		java.sql.Time.class,
        		int.class, 
        		float.class, 
        		double.class, 
        		boolean.class, 
        		long.class,
        		Integer.class,
        		java.util.List.class,
        		java.util.ArrayList.class,
        		java.util.Date.class
        		};
        
        this.stream.addPermission(NoTypePermission.NONE);
        this.stream.allowTypesByWildcard(new String[]{"com.stee.spfcore.messaging.**", "com.stee.spfcore.model.**", "com.stee.spfcore.vo.**"});
        this.stream.allowTypes(classes);
        //System.out.println("------ stream add A "+new Date());
        this.stream.registerConverter( new HibernateProxyConverter() );
        this.stream.registerConverter( new HibernatePersistentCollectionConverter( stream.getMapper() ) );
        this.stream.registerConverter( new HibernatePersistentMapConverter( stream.getMapper() ) );
        this.stream.registerConverter( new HibernatePersistentSortedMapConverter( stream.getMapper() ) );
        this.stream.registerConverter( new HibernatePersistentSortedSetConverter( stream.getMapper() ) );
        //System.out.println("------ stream add B "+new Date());
        this.stream.processAnnotations( MessageId.class );
        this.stream.processAnnotations( Heartbeat.class );

        for ( Class< ? > clazz : AnnotatedModelList.MODEL_LIST ) {
            this.stream.processAnnotations( clazz );
            //System.out.println("------ stream add C "+ clazz.getName()+" "+new Date());
        }
        System.out.println("> END message stream "+new Date());
    }

    public String write( Object object ) {
        return this.stream.toXML( object );
    }

    public Object read( String content ) {
        return this.stream.fromXML( content );
    }

    public String write( AbstractMessage message ) {
        StringWriter writer = new StringWriter();

        try {
            final ObjectOutputStream out = this.stream.createObjectOutputStream( writer );
            out.writeObject( message.getIdentity() );
            message.write( out );
            out.close();
        }
        catch ( Throwable throwable ) {
            throw new RuntimeException( "Unable to write message output: " + throwable.getMessage() );
        }

        return writer.toString();
    }

    /*public AbstractMessage read( String content, IMessagingContext context ) {
        StringReader reader = new StringReader( content );

        try {
            final ObjectInputStream in = this.stream.createObjectInputStream( reader );
            MessageId messageId = ( MessageId ) in.readObject();
            AbstractMessage message = context.create( messageId );

            if ( message != null ) {
                message.read( in );
            }

            return message;
        }
        catch ( Throwable throwable ) {
            throw new RuntimeException( "Unable to read message input: " + throwable.getMessage() );
        }
    }*/
}

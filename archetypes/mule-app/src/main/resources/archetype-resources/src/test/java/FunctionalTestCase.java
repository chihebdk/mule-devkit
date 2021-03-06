/**
 * This file was automatically generated by the Mule Development Kit
 */

#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )

package ${package};

import org.mule.api.MuleMessage;
import org.mule.api.client.MuleClient;
import org.mule.transport.NullPayload;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class FunctionalTestCase extends org.mule.tck.junit4.FunctionalTestCase
{
    protected String getConfigResources()
    {
        return "${artifactId}-test-config.xml";
    }

    @Test
    public void testCofiguration() throws Exception
    {
    	MuleClient client = muleContext.getClient();
        client.dispatch("vm://in", "some data", null);
        MuleMessage result = client.request("vm://out", RECEIVE_TIMEOUT);
        assertNotNull(result);
        assertNull(result.getExceptionPayload());
        assertFalse(result.getPayload() instanceof NullPayload);

        //TODO Assert the correct data has been received
        assertEquals("some data Received", result.getPayloadAsString());
    }
}

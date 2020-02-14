package works.template.samples;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class HelloWorldTest {

    private HelloWorld servlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        servlet = new HelloWorld();
    }

    @Test
    public void testDoGet() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);
        when(response.getWriter()).thenReturn(writer);

        servlet.doGet(request, response);
        assertEquals("Hello Anudeep!", new String( out.toByteArray(), "UTF-8"));
    }
}
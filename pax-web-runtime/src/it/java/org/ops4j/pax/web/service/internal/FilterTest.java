/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ops4j.pax.web.service.internal;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.junit.Test;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.NamespaceException;

import static org.easymock.EasyMock.*;

public class FilterTest extends IntegrationTests {

	@Test
	public void filterIsCalledOnUrlPattern()
			throws NamespaceException, ServletException, IOException {
		Servlet servlet = createMock(Servlet.class);
		servlet.init((ServletConfig) notNull());
		servlet.destroy();

		Filter filter = createMock(Filter.class);
		filter.init((FilterConfig) notNull());
		filter.doFilter((ServletRequest) notNull(), (ServletResponse) notNull(), (FilterChain) notNull());
		filter.destroy();

		replay(servlet, filter);

		HttpContext context = m_httpService.createDefaultHttpContext();
		m_httpService.registerServlet("/test", servlet, null, context);
		m_httpService.registerFilter(filter, new String[]{"/*"}, null, context);

		HttpMethod method = new GetMethod("http://localhost:8080/test");
		m_client.executeMethod(method);
		method.releaseConnection();

		m_httpService.unregister("/test");
		m_httpService.unregisterFilter(filter);

		verify(servlet, filter);
	}

	@Test
	public void filterIsCalledOnServlet()
			throws NamespaceException, ServletException, IOException {
		Servlet servlet = createMock(Servlet.class);
		servlet.init((ServletConfig) notNull());
		servlet.destroy();

		Filter filter = createMock(Filter.class);
		filter.init((FilterConfig) notNull());
		filter.doFilter((ServletRequest) notNull(), (ServletResponse) notNull(), (FilterChain) notNull());
		filter.destroy();

		replay(servlet, filter);

		HttpContext context = m_httpService.createDefaultHttpContext();
		m_httpService.registerServlet("/test", servlet, null, context);
		m_httpService.registerFilter(filter, null, new String[]{"/test"}, context);

		HttpMethod method = new GetMethod("http://localhost:8080/test");
		m_client.executeMethod(method);
		method.releaseConnection();

		m_httpService.unregister("/test");
		m_httpService.unregisterFilter(filter);

		verify(servlet, filter);
	}


}
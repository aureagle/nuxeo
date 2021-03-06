/*
 * (C) Copyright 2006-2008 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     arussel
 */
package org.nuxeo.ecm.platform.web.common.exceptionhandling.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Listener doing nothing.
 *
 * @author arussel
 */
public class NullExceptionHandlingListener implements ExceptionHandlingListener {

    public void beforeForwardToErrorPage(Throwable t, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
    }

    public void beforeSetErrorPageAttribute(Throwable t, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
    }

    public void startHandling(Throwable t, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
    }

    public void afterDispatch(Throwable t, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
    }

}

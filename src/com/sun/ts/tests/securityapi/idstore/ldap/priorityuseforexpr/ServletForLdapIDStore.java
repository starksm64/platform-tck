/*
 * Copyright (c) 2018, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package com.sun.ts.tests.securityapi.idstore.ldap.priorityuseforexpr;

import static jakarta.security.enterprise.identitystore.IdentityStore.ValidationType;
import static jakarta.security.enterprise.identitystore.IdentityStore.ValidationType.VALIDATE;
import jakarta.security.enterprise.identitystore.LdapIdentityStoreDefinition.LdapSearchScope;
import static jakarta.security.enterprise.identitystore.LdapIdentityStoreDefinition.LdapSearchScope.ONE_LEVEL;

import com.sun.ts.tests.securityapi.idstore.common.ConfigBean;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import jakarta.annotation.security.DeclareRoles;
import jakarta.security.enterprise.identitystore.LdapIdentityStoreDefinition;

import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jakarta.annotation.sql.DataSourceDefinition;

@LdapIdentityStoreDefinition(
    url = "ldap://localhost:11389/", 
    callerBaseDn = "ou=caller,dc=securityapi,dc=net", 
    groupSearchBase = "ou=group,dc=securityapi,dc=net", 
    groupSearchFilter = "(&(member=%s)(objectClass=groupOfNames))", 
    groupSearchScope = LdapSearchScope.ONE_LEVEL, 
    useFor = {ValidationType.VALIDATE}, 
    priority = 100, 
    priorityExpression = "#{configBean.priority300}", 
    useForExpression = "${configBean.useforBoth}"
)

/**
 * Test Servlet that prints out the name of the authenticated caller and whether
 * this caller is in any of the roles {Administrator, Manager, Employee}
 *
 */
@DeclareRoles({ "Administrator", "Manager", "Employee" })
@WebServlet("/ServletForLdapIDStore")
public class ServletForLdapIDStore extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    System.out.println("Inside ServletForLdapIDStore:doGet() ....." + "<BR>");

    String webName = null;
    if (request.getUserPrincipal() != null) {
      System.out.println("The user principal is: "
          + request.getUserPrincipal().getName() + "<BR>");
      webName = request.getUserPrincipal().getName();
    }

    response.getWriter().write("web username: " + webName + "\n");
  }

}

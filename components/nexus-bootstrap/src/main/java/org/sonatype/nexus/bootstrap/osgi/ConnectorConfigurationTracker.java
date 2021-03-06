/*
 * Sonatype Nexus (TM) Open Source Version
 * Copyright (c) 2008-present Sonatype, Inc.
 * All rights reserved. Includes the third-party code listed at http://links.sonatype.com/products/nexus/oss/attributions.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License Version 1.0,
 * which accompanies this distribution and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Sonatype Nexus (TM) Professional Version is available from Sonatype, Inc. "Sonatype" and "Sonatype Nexus" are trademarks
 * of Sonatype, Inc. Apache Maven is a trademark of the Apache Software Foundation. M2eclipse is a trademark of the
 * Eclipse Foundation. All other trademarks are the property of their respective owners.
 */
package org.sonatype.nexus.bootstrap.osgi;

import org.sonatype.nexus.bootstrap.jetty.ConnectorConfiguration;
import org.sonatype.nexus.bootstrap.jetty.JettyServer;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Watches for a {@link ConnectorConfiguration} services and registers them with {@link JettyServer}.
 *
 * @since 3.0
 */
public final class ConnectorConfigurationTracker
    extends ServiceTracker<ConnectorConfiguration, ConnectorConfiguration>
{
  private final JettyServer jettyServer;

  public ConnectorConfigurationTracker(BundleContext bundleContext, JettyServer jettyServer)
  {
    super(bundleContext, ConnectorConfiguration.class.getName(), null);
    this.jettyServer = jettyServer;
  }

  @Override
  public ConnectorConfiguration addingService(ServiceReference<ConnectorConfiguration> reference) {
    ConnectorConfiguration service = super.addingService(reference);
    jettyServer.addCustomConnector(service);
    return service;
  }

  @Override
  public void removedService(ServiceReference<ConnectorConfiguration> reference, ConnectorConfiguration service) {
    jettyServer.removeCustomConnector(service);
    super.removedService(reference, service);
  }
}

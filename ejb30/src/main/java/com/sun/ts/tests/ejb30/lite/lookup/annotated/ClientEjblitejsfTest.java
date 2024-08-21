package com.sun.ts.tests.ejb30.lite.lookup.annotated;

import com.sun.ts.tests.ejb30.lite.lookup.annotated.Client;
import java.net.URL;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;



@ExtendWith(ArquillianExtension.class)
@Tag("platform")
@Tag("ejb_web")
@Tag("web")
@Tag("tck-javatest")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientEjblitejsfTest extends com.sun.ts.tests.ejb30.lite.lookup.annotated.Client {
    static final String VEHICLE_ARCHIVE = "ejblite_lookup_annotated_ejblitejsf_vehicle";

        /**
        EE10 Deployment Descriptors:
        ejblite_lookup_annotated_ejblitejsf_vehicle_web: WEB-INF/beans.xml,WEB-INF/faces-config.xml,WEB-INF/web.xml
        ejblite_lookup_annotated_ejblitejsp_vehicle_web: 
        ejblite_lookup_annotated_ejbliteservlet_vehicle_web: WEB-INF/web.xml
        ejblite_lookup_annotated_ejbliteservlet2_vehicle_web: WEB-INF/web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/common/vehicle/ejblitejsf/ejblitejsf_vehicle_web.xml
        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static WebArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {

        // War
            // the war with the correct archive name
            WebArchive ejblite_lookup_annotated_ejblitejsf_vehicle_web = ShrinkWrap.create(WebArchive.class, "ejblite_lookup_annotated_ejblitejsf_vehicle_web.war");
            // The class files
            ejblite_lookup_annotated_ejblitejsf_vehicle_web.addClasses(
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.ejb30.lite.lookup.common.LookupBeanBase.class,
            com.sun.ts.tests.ejb30.lite.lookup.annotated.Client.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.ejb30.common.helper.Helper.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase.class,
            com.sun.ts.tests.ejb30.lite.lookup.annotated.Lookup2Bean.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.ejb30.common.lite.NumberIF.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.EJBLiteClientIF.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.ejb30.lite.lookup.annotated.HttpServletDelegate.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.ReasonableStatus.class,
            com.sun.ts.tests.ejb30.lite.lookup.common.LookupIF.class,
            com.sun.ts.tests.ejb30.lite.lookup.common.JsfClientBase.class,
            com.sun.ts.tests.ejb30.lite.lookup.common.ClientBase.class,
            com.sun.ts.tests.ejb30.lite.lookup.annotated.LookupBean.class,
            com.sun.ts.tests.ejb30.common.lite.NumberEnum.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteJsfClientBase.class,
            com.sun.ts.tests.ejb30.lite.lookup.annotated.JsfClient.class,
            com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.ejb30.lite.lookup.common.LookupBeanPlainBase.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/ejblitejsf_vehicle_web.xml");
            if(warResURL != null) {
              ejblite_lookup_annotated_ejblitejsf_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("//com/sun/ts/tests/common/vehicle/ejblitejsf/ejblitejsf_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              ejblite_lookup_annotated_ejblitejsf_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/lite/lookup/annotated/beans.xml");
            if(warResURL != null) {
              ejblite_lookup_annotated_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/beans.xml");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/beans.xml");
            if(warResURL != null) {
              ejblite_lookup_annotated_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/beans.xml");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/ejblitejsf_vehicle_web.xml");
            if(warResURL != null) {
              ejblite_lookup_annotated_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/ejblitejsf_vehicle_web.xml");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/lite/lookup/annotated/faces-config.xml");
            if(warResURL != null) {
              ejblite_lookup_annotated_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/faces-config.xml");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/faces-config.xml");
            if(warResURL != null) {
              ejblite_lookup_annotated_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/faces-config.xml");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/ejblitejsf_vehicle.xhtml");
            if(warResURL != null) {
              ejblite_lookup_annotated_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/ejblitejsf_vehicle.xhtml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(ejblite_lookup_annotated_ejblitejsf_vehicle_web, Client.class, warResURL);

        return ejblite_lookup_annotated_ejblitejsf_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void ejbPostConstructRecords() {
            super.ejbPostConstructRecords();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void ejb2PostConstructRecords() {
            super.ejb2PostConstructRecords();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void clientPostConstructRecords() {
            super.clientPostConstructRecords();
        }


}
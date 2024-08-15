package ee.jakarta.tck.persistence.core.StoredProcedureQuery;

import ee.jakarta.tck.persistence.core.StoredProcedureQuery.Client2;
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
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;



@ExtendWith(ArquillianExtension.class)
@Tag("persistence")
@Tag("platform")
@Tag("web")
@Tag("tck-javatest")

public class Client2PmservletTest extends ee.jakarta.tck.persistence.core.StoredProcedureQuery.Client2 {
    static final String VEHICLE_ARCHIVE = "jpa_core_StoredProcedureQuery_pmservlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        jpa_core_StoredProcedureQuery: myMappingFile.xml,META-INF/persistence.xml
        jpa_core_StoredProcedureQuery_appmanaged_vehicle_client: META-INF/application-client.xml
        jpa_core_StoredProcedureQuery_appmanaged_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_StoredProcedureQuery_appmanagedNoTx_vehicle_client: META-INF/application-client.xml
        jpa_core_StoredProcedureQuery_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_StoredProcedureQuery_pmservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_StoredProcedureQuery_puservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_StoredProcedureQuery_stateful3_vehicle_client: META-INF/application-client.xml
        jpa_core_StoredProcedureQuery_stateful3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_StoredProcedureQuery_stateless3_vehicle_client: META-INF/application-client.xml
        jpa_core_StoredProcedureQuery_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_StoredProcedureQuery_vehicles: 

        Found Descriptors:
        War:

        /com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive jpa_core_StoredProcedureQuery_pmservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "jpa_core_StoredProcedureQuery_pmservlet_vehicle_web.war");
            // The class files
            jpa_core_StoredProcedureQuery_pmservlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            ee.jakarta.tck.persistence.core.StoredProcedureQuery.Client2.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
            com.sun.ts.tests.common.vehicle.pmservlet.PMServletVehicle.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class
            );
            // The web.xml descriptor
            URL warResURL = Client2.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_StoredProcedureQuery_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client2.class.getResource("//com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              jpa_core_StoredProcedureQuery_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }
            // Web content
           archiveProcessor.processWebArchive(jpa_core_StoredProcedureQuery_pmservlet_vehicle_web, Client2.class, warResURL);

        // Par
            // the jar with the correct archive name
            JavaArchive jpa_core_StoredProcedureQuery = ShrinkWrap.create(JavaArchive.class, "jpa_core_StoredProcedureQuery.jar");
            // The class files
            jpa_core_StoredProcedureQuery.addClasses(
                ee.jakarta.tck.persistence.core.StoredProcedureQuery.Employee2.class,
                ee.jakarta.tck.persistence.core.StoredProcedureQuery.Employee.class,
                ee.jakarta.tck.persistence.core.StoredProcedureQuery.EmployeeMappedSC.class
            );
            // The persistence.xml descriptor
            URL parURL = Client2.class.getResource("persistence.xml");
            if(parURL != null) {
              jpa_core_StoredProcedureQuery.addAsManifestResource(parURL, "persistence.xml");
            }
            archiveProcessor.processParArchive(jpa_core_StoredProcedureQuery, Client2.class, parURL);
            // The orm.xml file
            parURL = Client2.class.getResource("orm.xml");
            if(parURL != null) {
              jpa_core_StoredProcedureQuery.addAsManifestResource(parURL, "orm.xml");
            }

        // Ear
            EnterpriseArchive jpa_core_StoredProcedureQuery_vehicles_ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_core_StoredProcedureQuery_vehicles.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jpa_core_StoredProcedureQuery_vehicles_ear.addAsModule(jpa_core_StoredProcedureQuery_pmservlet_vehicle_web);

            jpa_core_StoredProcedureQuery_vehicles_ear.addAsLibrary(jpa_core_StoredProcedureQuery);



            // The application.xml descriptor
            URL earResURL = Client2.class.getResource("/com/sun/ts/tests/jpa/core/StoredProcedureQuery/");
            if(earResURL != null) {
              jpa_core_StoredProcedureQuery_vehicles_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client2.class.getResource("/com/sun/ts/tests/jpa/core/StoredProcedureQuery/.ear.sun-application.xml");
            if(earResURL != null) {
              jpa_core_StoredProcedureQuery_vehicles_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            archiveProcessor.processEarArchive(jpa_core_StoredProcedureQuery_vehicles_ear, Client2.class, earResURL);
        return jpa_core_StoredProcedureQuery_vehicles_ear;
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void setParameterIntCalendarTemporalTypeTest() throws java.lang.Exception {
            super.setParameterIntCalendarTemporalTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void setParameterIntCalendarTemporalTypeIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.setParameterIntCalendarTemporalTypeIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void setParameterParameterCalendarTemporalTypeTest() throws java.lang.Exception {
            super.setParameterParameterCalendarTemporalTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void setParameterParameterCalendarTemporalTypeIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.setParameterParameterCalendarTemporalTypeIllegalArgumentExceptionTest();
        }


}
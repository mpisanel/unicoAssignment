# unicoAssignment

Configuration of message queue for wildfly server as below.

    <jms-destinations>
		<jms-queue name="unicoTestQueue">
			<entry name="jms/queue/unicoTestQueue"/>
			<entry name="java:jboss/exported/jms/queue/unicoTestQueue"/>
		</jms-queue>
	</jms-destinations>


Exposed web services

    Rest web services

    1. Exposed via jax_rs (Web module)

        Class Name: com.nihilent.rest.RestService
        URL       : http://{host}:{port}/unicoAssignment/restService

    Soap web services

    1. Exposed via jax_rs (Web module)

        Class Name: com.nihilent.soap
        URL       : http://{host}:{port}/unicoAssignment/SoapService?wsdl

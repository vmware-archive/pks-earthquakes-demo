# SpringOne PKS Earthquakes Demo

<img align="right" src="https://github.com/Pivotal-Field-Engineering/pks-earthquakes-demo/blob/master/docs/header-image.png" width="30%">

This demo is based on Fred Melo's awesome Earthquakes demo, found here: https://github.com/melofred/PKS-PCF-ElasticSearch

This version is intended for deployment at demo stations at SpringOne, and to showcase CF Routing and service binding. It is installed in the following locations:
<br><br>Orgs: **S1Pdemo11** and **S1Pdemo12**
<br>Space: **pks-earthquakes-demo**
<hr>

## Inspecting the PKS Cluster

The kubectl config should be installed at the demo station where you are working. If it is not, Pivotal employees can download it from here: https://drive.google.com/a/pivotal.io/file/d/1G5gVc3wNrD4ZD3Gxl8F9HxrHy__4pIzJ/view?usp=sharing and copy it into the **~/.kube/** folder on your local machine.

The kubectl proxy should be running in a terminal window at your demo station. If it is not, open a new terminal window and execute **kubectl proxy**

With the config installed, and the proxy running, you can now access the Kubernetes dashboard at the following address: http://localhost:8001/api/v1/namespaces/kube-system/services/kubernetes-dashboard/proxy

On the left-side navigation menu, click on deployments. Here you will see the deployments of Elastic Search and Kibana for the demo11 and demo12 environments.
<br><img src="https://github.com/Pivotal-Field-Engineering/pks-earthquakes-demo/blob/master/docs/deployments.png" width="50%"/>

These deployments expose two ports per pod, one for the API interface to elastic search, and one for the HTTP service for Kibana. PCF traditionally struggles with managing two exposed ports in a container, but Kubernetes give us flexibility here. We define two services (external network interfaces), each of which publishes a different port on the same deployment.
<br><img src="https://github.com/Pivotal-Field-Engineering/pks-earthquakes-demo/blob/master/docs/services.png" width="50%"/>

If we click on a service, we see that a label call **http-route-sync** has been applied to each one. This label triggers CF routing, and allows us to access each service over the internet through Pivotal Application Service.
<br><img src="https://github.com/Pivotal-Field-Engineering/pks-earthquakes-demo/blob/master/docs/elastic-service.png" width="30%"/><br><img src="https://github.com/Pivotal-Field-Engineering/pks-earthquakes-demo/blob/master/docs/kibana-service.png" width="30%"/><br>

So for the demo11 environment, you can access the services through the following URLs in your browser:
<br>Elastic Search: http://elastic-search-demo11.apps.pcf.corby.cc/
<br>Kibana: http://kibana-demo11.apps.pcf.corby.cc/
<hr>

## Running SCDF Streams

Log into Apps Manager. In the **pks-earthquakes-demo** space, inspect the services. In addition to RabbitMQ and MySQL, we have a user-provided service that will allow our apps to bind to the elastic search deployment that is exposed through CF routing. Click on the user-provided service and select configuration:
<br><img src="https://github.com/Pivotal-Field-Engineering/pks-earthquakes-demo/blob/master/docs/user-provided-service.png"/>

Now, let's bring up the Spring Cloud Data Flow dashboard. It can be accessed at the following URLs:<br>
**Demo11**: https://scdf-demo11.cfapps.io/dashboard<br>
**Demo12**: https://scdf-demo12.cfapps.io/dashboard<br>

Select Streams from the top menu bar. If you don't see the definition of the earthquakes stream, follow the instructions at [Creating the Earthquake Stream](https://github.com/Pivotal-Field-Engineering/pks-earthquakes-demo/blob/master/docs/create-stream.MD)

If the stream was already created, and you did not need to go through these steps, just hit the Deploy button to start it off:
<br><img src="https://github.com/Pivotal-Field-Engineering/pks-earthquakes-demo/blob/master/docs/deploy-stream.png" width="50%"/>

Now that the stream is deployed, you will see managed microservices running for each step in the stream. This stream can access our PKS deployment of Elastic Search as a user-provided service.
<br><img src="https://github.com/Pivotal-Field-Engineering/pks-earthquakes-demo/blob/master/docs/scdf-microservices.png" width="50%"/>
<hr>

## Visualizing the Data

If you need to reset the data from a previous run, you can use the **createIndex.sh** script in this repo. It takes one argument, which is the Elastic Search API endpoint. For example, to reset the data in the Demo11 environment, you would run:<br>
`./createIndex.sh elastic-search-demo11.apps.pcf.corby.cc`<br>
This script can be safely executed at any time.

Log into the Kibana UI, at the published endpoint (e.g. in Demo11 you go to http://kibana-demo11.apps.pcf.corby.cc/). Select Dashboards from the left-side menu, and click on the Earthquake dashboard.

If you don't see Dashboard options, you will need to [Create the earthquake dashboard](https://github.com/Pivotal-Field-Engineering/pks-earthquakes-demo/blob/master/docs/create-dashboard.MD).

You will now see a live view of the data that is being ingested by Spring Cloud Dataflow.
<img src="https://github.com/Pivotal-Field-Engineering/pks-earthquakes-demo/blob/master/docs/header-image.png" width="50%">

To swich to an auto-update animation, click on the clock icon at the top of the page, and select **Auto-refresh**
<img src="https://github.com/Pivotal-Field-Engineering/pks-earthquakes-demo/blob/master/docs/auto-refresh.png" width="25%">

Select an update interval of 5 seconds, sit back, and take a break.

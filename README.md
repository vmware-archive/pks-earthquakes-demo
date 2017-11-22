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

Log into Apps Manager. In the **pks-eqarthquakes-demo** space, inspect the services. In addition to RabbitMQ and MySQL, we have a user-provided service that will allow our apps to bind to the elastic search deployment that is exposed through CF routing. Click on the user-provided service and select configuration:
<br><img src="https://github.com/Pivotal-Field-Engineering/pks-earthquakes-demo/blob/master/docs/user-provided-service.png"/>

Now, let's bring up the Spring Cloud Data Flow dashboard. It can be accessed at the following URLs:<br>
**Demo11**: https://scdf-demo11.cfapps.io/dashboard<br>
**Demo12**: https://scdf-demo12.cfapps.io/dashboard<br>

Select Streams from the top menu bar. If the earthquakes stream has not already been defined, you can select the **Create Stream** folder tab to define it. To create it, copy and paste the following text into the text box marked "Enter stream definition here...":

`time --fixed-delay=3000 | httpclient --url-expression='''http://s3.amazonaws.com/scdf-apps/earthquakes.txt''' | splitter --delimiters=\"*\" | elastic-search`

You should now see a visual display of your stream in the designer.
<br><img src="https://github.com/Pivotal-Field-Engineering/pks-earthquakes-demo/blob/master/docs/earthquakes-stream.png" width="50%"/>

After you see this, click the 'Create Stream' button. This will bring up a confirmation dialog where you enter the name of the stream ('earthquakes') and check the Deploy stream box before hitting Create:
<br><img src="https://github.com/Pivotal-Field-Engineering/pks-earthquakes-demo/blob/master/docs/confirm-stream.png" width="50%"/>

If the stream was already created, and you did not need to go through these steps, just hit the Deploy button to start it off:
<br><img src="https://github.com/Pivotal-Field-Engineering/pks-earthquakes-demo/blob/master/docs/deploy-stream.png" width="50%"/>

Now that the stream is deployed, you will see managed microservices running for each step in the stream. This stream can access our PKS deployment of Elastic Search as a user-provided service.
<br><img src="https://github.com/Pivotal-Field-Engineering/pks-earthquakes-demo/blob/master/docs/scdf-microservices.png" width="50%"/>
<hr>

## Visualizing the Data

If you need to reset the data from a previous run, you can use the **createIndex.sh** script in this repo. It takes one argument, which is the Elastic Search API endpoint. For example, to reset the data in the Demo11 environment, you would run:<br>
`./createIndex.sh elastic-search-demo11.apps.pcf.corby.cc`<br>
This script can be safely executed at any time.

Log into the Kibana UI, at the published endpoint (e.g. in Demo11 you go to http://kibana-demo11.apps.pcf.corby.cc/). Select Dashboards from the left-side menu, and click on the Earthquakes dashboard.

If you don't see Dashboard options, you will need to create the earthquake dashboard. To do this, click on the Management left-side menu option, and select Index Patterns from the top-side menu. In the **Index name or pattern** field, enter **earthquakes**
<br><img src="https://github.com/Pivotal-Field-Engineering/pks-earthquakes-demo/blob/master/docs/index-patterns.png" width="50%"/>

Click the create button. Now from the Management screen, select Saved Objects from the top-side menu. Click the Import button in the top-right, and upload the earthquakes-dashboard.json file from this repo. Now you can go to the Dashboard and select the Earthquakes dashboard.

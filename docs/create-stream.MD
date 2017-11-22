# Creating the Earthquakes Stream

From the Streams page, select the **Create Stream** folder tab to define it. To create it, copy and paste the following text into the text box marked "Enter stream definition here...":

`time --fixed-delay=3000 | httpclient --url-expression='''http://s3.amazonaws.com/scdf-apps/earthquakes.txt''' | splitter --delimiters=\"*\" | elastic-search`

You should now see a visual display of your stream in the designer.
<br><img src="https://github.com/Pivotal-Field-Engineering/pks-earthquakes-demo/blob/master/docs/earthquakes-stream.png" width="50%"/>

After you see this, click the 'Create Stream' button. This will bring up a confirmation dialog where you enter the name of the stream ('earthquakes') and check the Deploy stream box before hitting Create:
<br><img src="https://github.com/Pivotal-Field-Engineering/pks-earthquakes-demo/blob/master/docs/confirm-stream.png" width="50%"/>

**RAP mobile Train Dashboard**  
An open source demo for [RAP mobile](http://rapmobile.eclipsesource.com). The dashboard shows german train information for given dates. The information used comes from the [OpenDataCity "Zugmonitor API"](http://www.opendatacity.de/zugmonitor-api/).

**How to run it**
After cloning this repository and importing the project into your Eclipse workspace, there are two things you have to do to run the demo. The first thing is to set the right target platform. For this you can used the *rap-mobile-dashboard.target* target definition file located in the *com.eclipsesource.train.dashboard.ui* project. The second thing is to configure the application with two system properties:

* **com.eclipsesource.train.dashboard.data.location** (String) Needs to be a writable folder location to store the train data in. 
* **com.eclipsesource.train.dashboard.max.age** (int) Defines the maximum age of today's data because it can change during the day.

After this you can launch the application busing the *RAP Mobile Dashboard.launch* launch configuration file located in the *com.eclipsesource.train.dashboard.ui* project.

**License**  
All files are licensed under the [Eclipse Public License - v 1.0](http://www.eclipse.org/legal/epl-v10.html)



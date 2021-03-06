# springboot-rabbitmq-sample
An example of spring boot application with rabbitmq and spring amqp

* Install rabbitmq

	You can use a docker image or install on a VM using VirtualBox.
This is the procedure for centos 7.

	yum -y update
	yum -y install epel-release
	yum -y install gcc gcc-c++ glibc-devel make ncurses-devel openssl-devel autoconf java-1.8.0-openjdk-devel git wget wxBase.x86_64

	wget http://packages.erlang-solutions.com/erlang-solutions-1.0-1.noarch.rpm
	rpm -Uvh erlang-solutions-1.0-1.noarch.rpm
	yum -y update
	yum -y install erlang

	wget https://www.rabbitmq.com/releases/rabbitmq-server/v3.6.1/rabbitmq-server-3.6.1-1.noarch.rpm
	rpm --import https://www.rabbitmq.com/rabbitmq-signing-key-public.asc
	yum -y install rabbitmq-server-3.6.1-1.noarch.rpm

* Configure rabbitmq

	chkconfig --list
	chkconfig rabbitmq-server on
	systemctl start rabbitmq-server

	rabbitmqctl add_user admin admin
	rabbitmqctl set_user_tags admin administrator
	rabbitmqctl set_permissions -p / admin ".*" ".*" ".*"

	rabbitmqctl add_user darty darty
	rabbitmqctl add_vhost darty-vhost
	rabbitmqctl set_permissions -p darty-vhost darty ".*" ".*" ".*"
	rabbitmqctl set_permissions -p darty-vhost admin ".*" ".*" ".*"

	rabbitmq-plugins enable rabbitmq_management

* Rabbitmq web management

	You can access the rabbitmq management interface on our browser : http://<your_vm_ip>:15672
user/pwd : darty/darty or admin/admin

* Launch consumers/publishers

	Use your beloved java editor or on the command line :

	In your local maven repository or project target folder launch:

	nohup java -jar order-creator-0.0.1-SNAPSHOT.jar&
	
	nohup java -jar order-validator-0.0.1-SNAPSHOT.jar&
	
	nohup java -jar order-finalizer-0.0.1-SNAPSHOT.jar&
	
	nohup java -jar order-emailer-0.0.1-SNAPSHOT.jar&
	
	nohup java -jar order-injector-0.0.1-SNAPSHOT.jar&

	to kill all : pkill -f order*jar

* The use case

	* step 1 : order-creator creates orders	
	* step 2 : order-validator validates the orders	
	* step 3 : order-finalizer finalize the orders	
	* step 4 : we are adding a new consumer for sending emails : order-emailer
	
	order-injector is used to inject order creation messages

![Image of Yaktocat](https://github.com/fakarakas/springboot-rabbitmq-sample/raw/master/use-case.png)

# CITE Service Virtual Machine


## Requires:

- [Vagrant](https://www.vagrantup.com)
- [VirtualBox](https://www.virtualbox.org)

## Running the VM

1. Install Vagrant and Virtualbox.
1. In the `ecomparatio-cite/VirtualMachine/` directory, run `vagrant up`.
1. Wait… it will download many files and configure a virtual machine.
1. It will tell you when it is done.

## Running the CiteService from the Virtual Machine

1. In the `ecomparatio-cite/VirtualMachine/` directory, `vagrant ssh`
1. `cd /vagrant/ecomparatio-cite/CiteService`
1. `java -jar scs.jar`

## Accessing the CiteService

1. The SCS Service will be running at: `http://192.168.33.10:9000`
1. The file `VM-Examples.html` (or `VM-Examples.md`) includes a number of demonstration links.

## Quitting the Virtual Machine

1. If you have `ssh`ed into it, `logout`.
1. `vagrant halt`.




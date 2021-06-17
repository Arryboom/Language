
#Error: unknown flag: --link
>https://stackoverflow.com/questions/67126730/in-a-rootless-podman-setup-how-to-communicate-between-containers-in-different-p


Podman doesn't have anything like the "services" concept in Swarm or Kubernetes to provide for service discovery between pods. Your options boil down to:

1. Run both pods in the same network namespace, or
2. Expose the services by publishing them on host ports, and then access them via the host

For the first solution, we'd start by creating a network:

```
podman network create shared
```

And then creating both pods attached to the `shared` network:

```
podman pod create --name pod1 --network shared
podman pod create --name pod2 --network shared
```

With both pods running on the same network, containers can refer to the other pod by name. E.g, if you were running a web service in `p1c1` on port 80, in `p2c1` you could `curl http://pod1`.

For the second option, you would do something like:

```
podman pod create --name pod1 -p 1234:1234 ...
podman pod create --name pod2 ...
```

Now if `p1c1` has a service listening on port `1234`, you can access that from `p2c1` at `<some_host_address>:1234`.

* * *

> If I'm interpreting option 1 correctly, if the applications in p1c1 and p2c1 both use, say, port 8080; then there won't be any conflict anywhere (either within the pods and the outer host) IF I publish using something like this: 8080:8080 for app in p1c1 and 8081:8080 for app in p2c1? Is this interpretation correct?

That's correct. Each pod runs with its own network namespace (effectively, it's own ip address), so services in different pods can listen on the same port.

> Can the network (not ports) of a pod be reassigned once running? REASON: I'm using podman-compose(1), which creates things for you in a pod, but I may need to change things (like the network assignment) after the fact. Can this be done?

In general you cannot change the configuration of a pod or a container; you can only delete it and create a new one. Assuming that `podman-compose` has relatively complete support for the `docker-compose.yaml` format, you should be able to set up the network correctly in your `docker-compose.yaml` file (you would create the network manually, and then reference it as an `external` network in your compose file).

[Here](https://docs.docker.com/compose/networking/#use-a-pre-existing-network) is a link to the relevant Docker documentation. I haven't tried this myself with podman.

##another way

add-host works fine - the issue is that the container never had a mapping from its hostname to localhost, due to being created in a pod with --infra=true (default value). I don't know what should be changed here (creating the pod with ``--infra=false``, set ``--add-host ${CONTAINER_ID}:127.0.0.1``, or ``--hostname=${SERVICE_NAME})``, but one of these should probably be done.
https://github.com/containers/podman-compose/issues/165
##another way
```
Are you able to put them in a pod/create them in the same network
namespace? Then you could have the containers talk over localhost and not
have to worry about finding the IP.
You can also specify a static ip with --ip on container creation for the
container that needs to be pinged, and then use that IP for the other
container. That could "fix" the container IP, and allow them to communicate
that way rather than name.

Do either of these options help? podman currently doesn't do
intra-container network resolution by name (as you've noticed).
```
##and another way

I think you're going about this wrong. Everytime you create a pod, you're creating a separate namespace for a group of containers. Attaching to a network doesn't do what you want in this instance, and honestly I'm not sure how you did that. My version of podman doesn't recognize --network in the pod create call. Which means, whatever you did here is impossible anyway.

To prove my point, run the following:

podman pod create --name nexus

podman pod inspect nexus

If you look at the containers listed as part of the pod, you'll already see one container associated with it called "Infra". This container just runs the pause command, and basically holds the namespace so other containers can join it.

URL: [https://developers.redhat.com/blog/2019/01/15/podman-managing-containers-pods/](https://developers.redhat.com/blog/2019/01/15/podman-managing-containers-pods/)

So, here's how I successfully did what you wanted here:

- podman pod create --name test
    
- podman run -d --pod test --name testPod1 --hostname testPod1 --network foobar -e MYSQL\_ROOT\_PASSWORD=PASS mariadb
    
- podman inspect testPod1 |grep IPAddress
    
- podman run -it --pod test --name testPod2 --hostname testPod2 --network foobar --add-host testPod1:10.89.0.5 busybox sh
    

Far as I know, regardless of whether or not you're part of the same network you can't refer to something by name without a connection between the boxes somehow. I tried just pinging to testPod1 like you tried earlier and I got the same error. I suspect this is because:

1. /etc/hosts is generated on creation of the container and is only modified by calls to --add-host and/or the initial IP -> host mapping of the container itself.
    
2. DNS is overwritten by the HOST /etc/resolv.conf automatically in containers. Therefore what you think you're getting when you ping testPod1 is in fact a DNS call to your network DNS, and then failing down to /etc/hosts which has no entry for testPod1 yet.


>(may wrong)You have to use the pods hostname to connect. All containers inside the pod have the same hostname (pod name).

---
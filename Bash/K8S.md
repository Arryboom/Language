#all node

###Stop swap

```
swapoff -a
sed -ri 's/.*swap.*/#&/' /etc/fstab
```

###setup basic

```
apt-get install -y bridge-utils
modprobe overlay
modprobe br_netfilter
lsmod | grep netfilter
```

sysctl

```
net.bridge.bridge-nf-call-iptables  = 1
net.ipv4.ip_forward                 = 1
net.bridge.bridge-nf-call-ip6tables = 1
```


###CRI-Docker Installation Instructions(all machine)
>https://github.com/cri-o/cri-o/blob/main/install.md#readme


```
export OS=xUbuntu_20.04
export VERSION=1.19

echo 'deb http://deb.debian.org/debian buster-backports main' > /etc/apt/sources.list.d/backports.list
apt update
apt install -y -t buster-backports libseccomp2 || apt update -y -t buster-backports libseccomp2

echo "deb [signed-by=/usr/share/keyrings/libcontainers-archive-keyring.gpg] https://download.opensuse.org/repositories/devel:/kubic:/libcontainers:/stable/$OS/ /" > /etc/apt/sources.list.d/devel:kubic:libcontainers:stable.list
echo "deb [signed-by=/usr/share/keyrings/libcontainers-crio-archive-keyring.gpg] https://download.opensuse.org/repositories/devel:/kubic:/libcontainers:/stable:/cri-o:/$VERSION/$OS/ /" > /etc/apt/sources.list.d/devel:kubic:libcontainers:stable:cri-o:$VERSION.list



mkdir -p /usr/share/keyrings
curl -L https://download.opensuse.org/repositories/devel:/kubic:/libcontainers:/stable/$OS/Release.key | gpg --dearmor -o /usr/share/keyrings/libcontainers-archive-keyring.gpg
curl -L https://download.opensuse.org/repositories/devel:/kubic:/libcontainers:/stable:/cri-o:/$VERSION/$OS/Release.key | gpg --dearmor -o /usr/share/keyrings/libcontainers-crio-archive-keyring.gpg

apt-get update
apt-get install cri-o cri-o-runc

```

/etc/crio/crio.conf.d/02-cgroup-manager.conf

```
[crio.runtime]
conmon_cgroup = "pod"
cgroup_manager = "cgroupfs"
```


edit /etc/crio/crio.conf


```
[crio.image]
pause_image="k8s.gcr.io/pause:3.2"
```


start service,enable
```
systemctl start crio
systemctl enable crio
```


###Installing kubeadm, kubelet and kubectl

```
apt-get install -y apt-transport-https ca-certificates curl
sudo curl -fsSLo /usr/share/keyrings/kubernetes-archive-keyring.gpg https://packages.cloud.google.com/apt/doc/apt-key.gpg
echo "deb [signed-by=/usr/share/keyrings/kubernetes-archive-keyring.gpg] https://apt.kubernetes.io/ kubernetes-xenial main" | sudo tee /etc/apt/sources.list.d/kubernetes.list

apt-get update
apt install -y kubelet=1.19.6-00 kubeadm=1.19.6-00 kubectl=1.19.6-00
apt-mark hold kubelet kubeadm kubectl
```

#Manager


```
kubeadm init \
--apiserver-advertise-address=192.168.150.128 \
--pod-network-cidr=10.244.0.0/16 \
--cri-socket="unix:///var/run/crio/crio.sock"
```

```

Your Kubernetes control-plane has initialized successfully!

To start using your cluster, you need to run the following as a regular user:

  mkdir -p $HOME/.kube
  sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
  sudo chown $(id -u):$(id -g) $HOME/.kube/config

You should now deploy a pod network to the cluster.
Run "kubectl apply -f [podnetwork].yaml" with one of the options listed at:
  https://kubernetes.io/docs/concepts/cluster-administration/addons/

Then you can join any number of worker nodes by running the following on each as root:

kubeadm join 192.168.150.128:6443 --token kne7s5.4yo284ecga53nvjb \
    --discovery-token-ca-cert-hash sha256:f652e8011e1caf5d617d00bb70b13510346f83568861a73b56a03f807988257b
```


```
kubectl --kubeconfig=/etc/kubernetes/admin.conf create -f https://docs.projectcalico.org/v3.14/manifests/calico.yaml
kubeadm token create --print-join-command
```

````
  mkdir -p $HOME/.kube
  sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
  sudo chown $(id -u):$(id -g) $HOME/.kube/config

````

#worker
```
kubeadm join 192.168.150.128:6443 --token kne7s5.4yo284ecga53nvjb \
    --discovery-token-ca-cert-hash sha256:f652e8011e1caf5d617d00bb70b13510346f83568861a73b56a03f807988257b
```


>As stated in the comments by the community this indeed looks like a documentation error as there is no relation between kubeadm and kubectl when joining the nodes. For worker nodes you just need to install both kubelet and kubeadm.



-------------above might wrong in newest version---------------------














-----------

#status check

```
systemctl status cri-o.service
systemctl status kubelet
kubectl version --client
kubectl cluster-info
kubectl cluster-info dump
kubelet  --cgroup-driver=systemd version
service crio status
kubectl cluster-info
kubectl get pods -A
kubectl describe pod my-alpine

```
>crictl info
```
#:/home/ubuntu# crictl info
{
  "status": {
    "conditions": [
      {
        "type": "RuntimeReady",
        "status": true,
        "reason": "",
        "message": ""
      },
      {
        "type": "NetworkReady",
        "status": true,
        "reason": "",
        "message": ""
      }
    ]
  }
}
```

#issue

```
http://deb.debian.org/debian buster-backports InRelease
  The following signatures couldn't be verified because the public key is not available: NO_PUBKEY 648ACFD622F3D138 NO_PUBKEY 0E98404D386FA1D9
```
```
sudo apt-key adv --keyserver keyserver.ubuntu.com --recv-keys 0E98404D386FA1D9
sudo apt-key adv --keyserver keyserver.ubuntu.com --recv-keys 648ACFD622F3D138
```
or *keyring.debian.org*





>A single cgroup manager simplifies the view of what resources are being allocated and will by default have a more consistent view of the available and in-use resources. When there are two cgroup managers on a system, you end up with two views of those resources. In the field, people have reported cases where nodes that are configured to use cgroupfs for the kubelet and Docker, but systemd for the rest of the processes, become unstable under resource pressure.




reinit

```
kubeadm reset -f
iptables -F && iptables -t nat -F && iptables -t mangle -F && iptables -X

```



Note:

Kubeadm uses the same KubeletConfiguration for all nodes in the cluster. The KubeletConfiguration is stored in a ConfigMap object under the kube-system namespace.

Executing the sub commands init, join and upgrade would result in kubeadm writing the KubeletConfiguration as a file under /var/lib/kubelet/config.yaml and passing it to the local node kubelet.


>https://jbn1233.medium.com/docker-cri-o-behind-http-proxy-4a5645a9ff7b



# docker / cri-o behind http proxy

Just insert proxy setting into docker / cri-o systemd config.

```
Environment="HTTP_PROXY=socks5://192.168.150.1:1992/" "HTTPS_PROXY=socks5://192.168.150.1:1992/" "NO_PROXY=localhost,127.0.0.1,10.0.0.0/8,172.16.0.0/12,192.168.0.0/16"
```



Environment="HTTP_PROXY=http://proxy.home.net:80/" "HTTPS_PROXY=http://proxy.home.net:80" "NO_PROXY=north.home.net,.home.net,localhost,127.0.0.1,10.0.0.0/8,172.16.0.0/12,192.168.0.0/16"

e.g. /usr/lib/systemd/system/crio.service

at \[Service\] section.



##kubetools must be same version with crio




>https://github.com/justmeandopensource/kubernetes/blob/master/docs/install-cluster-ubuntu-20.md






#group not health
```
root@kman:~/.kube# kubectl get cs
Warning: v1 ComponentStatus is deprecated in v1.19+
NAME                 STATUS      MESSAGE                                                                                       ERROR
scheduler            Unhealthy   Get "http://127.0.0.1:10251/healthz": dial tcp 127.0.0.1:10251: connect: connection refused
controller-manager   Unhealthy   Get "http://127.0.0.1:10252/healthz": dial tcp 127.0.0.1:10252: connect: connection refused
etcd-0               Healthy     {"health":"true"}    
```
>https://blog.csdn.net/xiaobao7865/article/details/107513957


v1.20 deprecated,dont have to pay attention


#使用kubernetes创建容器一直处于ContainerCreating状态的原因查找与解决

https://blog.csdn.net/gsying1474/article/details/53256599/



#crio doesnt support export import,but here is some tools
https://github.com/kubernetes-sigs/cri-tools/issues/546


```
apt-get -y install skopeo
```

```
# Saving
skopeo copy containers-storage:docker.io/weaveworks/weave-npc:2.8.1 dir:image-output-dir
# Loading
skopeo copy dir:image-output-dir containers-storage:docker.io/weaveworks/weave-npc:2.8.1
```



nodePort:30001是Kubernetes在每个Node上打开的一个端口并且每个Node的端口都是一样的，通过\<NodeIP>:NodePort的方式Kubernetes集群外部的程序可以访问Service。










#k8s node节点停机维护，pod如何迁移？
https://blog.csdn.net/yanggd1987/article/details/108139436

**默认迁移
当node节点关机后，k8s集群并没有立刻发生任何自动迁移动作，如果该node节点上的副本数为1，则会出现服务中断的情况。其实事实并非如此，k8s在等待5分钟后，会自动将停机node节点上的pod自动迁移到其他node节点上。**

# 1.模拟node节点停机，停止kubelet
systemctl stop kubelet

## 手动迁移

为避免等待默认的5分钟，我们还可以使用cordon、drain、uncordor三个命令实现节点的主动维护。此时需要用到以下三个命令：

- cordon：标记节点不可调度，后续新的pod不会被调度到此节点，但是该节点上的pod可以正常对外服务；
- drain：驱逐节点上的pod至其他可调度节点；
- uncordon：标记节点可调度；
As part of GSOC 2019, [Athina Plaskasoviti implemented `--cloud-init` support for virt-install](https://athinapl.home.blog/2019/08/25/gsoc-2019-cloud-init-configuration-for-virt-manager-virt-install/). This post provides a bit more info about the feature.

#### Why cloud-init

For a long while, most mainstream Linux distros have shipped 'cloud images': `raw` or `qcow2` formatted disk images with the distro minimally pre-installed on it. These images typically have [cloud-init](https://cloud-init.io/) set to run on VM bootup. cloud-init can do a variety of things, like add users, change passwords, register ssh keys, and generally perform any desired action on the VM OS. This only works when cloud-init is passed the right configuration by whatever platform is starting the VM, like OpenStack or virt-install. cloud-init supports many different [datasources](https://cloudinit.readthedocs.io/en/latest/topics/datasources.html) for getting configuration outside the VM.

Historically though the problem is that slapping these images into virt-install or virt-manager gives crappy results, because these tools were not providing any datasource. In this case, cloud-init reverts to its distro default configured behavior, which in most cases is unusable. For example on Fedora, the result was: hang waiting for cloud-init data, time out, drop to login prompt with all accounts locked.

Prior to virt-install `--cloud-init` support, the simplest workaround was to use libguestfs, specifically `virt-customize`, to set a root password and disable cloud-init:

`$ virt-customize -a MY-CLOUD-IMAGE.qcow2 \
    --root-password password:SUPER-SECRET-PASSWORD \
    --uninstall cloud-init` 

#### \--cloud-init option

The `--cloud-init` option will tell virt-install to set up a [nocloud](https://cloudinit.readthedocs.io/en/latest/topics/datasources/nocloud.html) datasource via a specially formatted `.iso` file that is generated on the fly, and only used for the first VM bootup.

The default behavior when `--cloud-init` is specified with no suboptions will do the following:

-   Generate a random root password and print it to the terminal.
-   Default to VM serial console access rather than graphical console access. Makes it easier to paste the password and gives more ssh-like behavior.
-   Sets the root password to expire on first login. So the temporary password is only used once.
-   Disables cloud-init for subsequent VM startups. Otherwise on the next VM boot you'd face locked accounts again.

The `--cloud-init` also has suboptions to specify your own behavior, like transfer in a host ssh public key, pass in raw cloud-init `user-data`/`meta-data`, etc. See the [virt-install --cloud-init man page section](https://github.com/virt-manager/virt-manager/blob/master/man/virt-install.rst#--cloud-init) for the specifics.

#### Room for improvement

This is all a step in the right direction but there's lots more we can do here:

-   Extend virt-install's `--install` option to learn how to fetch cloud images for the specified distro. [libosinfo](https://libosinfo.org/) and [osinfo-db](https://gitlab.com/libosinfo/osinfo-db) already track cloud image download links for many distros so the info we need is already in place. We could make `virt-install --install fedora32,cloud=yes` a single way to pull down a cloud image, generate a cloud-init datasource, and create the VM in one shot.
-   Use `--cloud-init` by default when the user passes us a cloud-init enabled disk image. `virt-customize` has a lot of disk image detection smarts already, but we aren't using that in virt-install yet.
-   virt-manager UI support. There's an [issue tracking this](https://github.com/virt-manager/virt-manager/issues/143) with some more thoughts in it.
-   Similar support for [CoreOS Ignition](https://coreos.com/ignition/) which fulfills a similar purpose as cloud-init for distros like [Fedora CoreOS](https://getfedora.org/en/coreos/). There's an [issue tracking this too](https://github.com/virt-manager/virt-manager/issues/152).

I personally don't have plans to work on these any time soon, but I'm happy to provide guidance if anyone is interested in helping out!
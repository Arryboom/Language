#on windows
when use git desktop to commit a repo.it told me that some file was too large more than 100mb and cannot proceed,but it necessary file for this repo.so here is the way could work

1.install git-scm on windows,make sure you have git bash enabled.
2.download and install git-lfs
https://git-lfs.github.com/
after extract the file in it.installed with 
- ``git bash here`` to open a git bash command console in git-lfs directory
- ``./install.sh`` to install git-lfs
- now you should got git-lfs.exe available in git bash and windows cmd console 
3.follow the docs in https://git-lfs.github.com/
in your repo dir,
- ``git lfs track "*.psd"`` track the large files extension name
-  ``git add .gitattributes`` add the gitattributes file
4.use git desktop as usual(it may uploaded slowly since you add **large file** to the repo).

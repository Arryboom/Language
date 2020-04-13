# deploy on production
https://discuss.pytorch.org/t/pytorch-models-for-production/18115


### 1

It depends on the platform to which you’re aiming to deploy and some other constraints, for example if your use case can be fulfilled via REST or similar service and you don’t mind the python overhead you could potentially use PyTorch as it is on a server to handle web requests. However if you’re aiming for edge deployment or you want to squeeze as much raw performance as possible, your best bet right now is to use ONNX 30. You can export 5 your PyTorch model in ONNX format and then use another framework (like caffe2, MXNet, CNTK, etc.) to actually run the model, these other frameworks do support edge and/or have specialized deployment extensions (e.g. the MXNet model server 20 which can also serve ONNX models directly).

BTW PyTorch 1.0 58 is coming and will be production-ready, and I’m very excited about that! :smiley:

### 2

https://github.com/NirantK/pytorch-web-deploy.git




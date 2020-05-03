#deploy

>https://www.cnblogs.com/liuzhongrong/p/12361657.html


# [36.0 深度学习模型部署及在线预测 - 子颢的博客](https://www.cnblogs.com/liuzhongrong/p/12361657.html)

我们在前面的内容中讲到过，可以使用tf.train.Saver.save()和tf.train.Saver.restore()方法保存和恢复模型变量，但是这只是在模型训练过程中用来做checkpoint，保存的也只是模型的变量。只有导出整个模型（除了模型变量、还包括模型计算图和图的元数据），才能做模型部署和在线预测，这时就必须使用SavedModel（也可以导出Session Bundle，但是TensorFlow1.0后被废弃）来保存和加载模型。使用SavedModel做模型导出的代码如下：

```
print('Configuring CNN model...')
config = TCNNConfig()
if not os.path.exists(vocab_dir):  # 如果不存在词汇表，重建
    build_vocab(total_dir, vocab_dir)
categories, cat_to_id = read_category()
words, word_to_id = read_vocab(vocab_dir)

# 导出资源文件：词典表
print('start jsondump word_to_id...')
word_to_id_json = json.dumps(word_to_id, ensure_ascii=False)
word_to_id_json_dir = os.path.join(base_dir, 'word_to_id.json')
open_file(word_to_id_json_dir, mode='w').write(word_to_id_json)
print('finish jsondump word_to_id...')

# 导出资源文件：label标签
print('start jsondump cate_to_id...')
id_to_cate = {v: k for k, v in cat_to_id.items()}
cate_to_id_json = json.dumps(id_to_cate, ensure_ascii=False)
cate_to_id_json_dir = os.path.join(base_dir, 'label_index.json')
open_file(cate_to_id_json_dir, mode='w').write(cate_to_id_json)
print('finish jsondump cate_to_id...')

config.vocab_size = len(words)
config.num_classes = len(categories)
model = TextCNN(config)

# 训练模型
train()
# 测试
test()

# 导出模型
export_path = 'export'
print('Exporting trained model to: ', export_path)
session_export = tf.Session()
# session_export.run(tf.global_variables_initializer())
saver = tf.train.Saver(tf.global_variables(), sharded大专栏  36.0 深度学习模型部署及在线预测 - 子颢的博客 class="p">=True)
saver.restore(sess=session_export, save_path=save_path)  # 读取保存的模型
builder = tf.saved_model.builder.SavedModelBuilder(export_path)

tensor_info_x = tf.saved_model.utils.build_tensor_info(model.input_x)
tensor_info_y = tf.saved_model.utils.build_tensor_info(model.y_pred_cls)
tensor_info_score = tf.saved_model.utils.build_tensor_info(model.y_sore)
prediction_signature = (
    tf.saved_model.signature_def_utils.build_signature_def(
        inputs={'texts': tensor_info_x},
        outputs={'pred_cates': tensor_info_y,
                 'pred_score': tensor_info_score},
        method_name=tf.saved_model.signature_constants.PREDICT_METHOD_NAME))
legacy_init_op = tf.group(tf.tables_initializer(), name='legacy_init_op')
builder.add_meta_graph_and_variables(
    session_export, [tf.saved_model.tag_constants.SERVING],
    signature_def_map={
        'predict_categories': prediction_signature
    },
    legacy_init_op=legacy_init_op)
builder.save()
print('Done exporting!')

```

导出的模型文件目录结构如下：

```
variables/
    variables.data-?????-of-?????
    variables.index
saved_model.pb

```

variables就是模型变量，saved\_model.pb即为模型计算图定义。

# 模型部署

模型导出完成后，下一步是部署模型为在线服务，这时候需要用到TensorFlow Serving，它能为模型提供对外rpc调用的接口，以实现跨语言调用。但是首先需要安装TensorFlow Serving，安装完成后调用以下命令，即可启动服务：

```
tensorflow_model_server --port=port-numbers --model_name=your-model-name --model_base_path=your_model_base_path

```

*   port：指定RPC服务监听端口号。
*   model\_name：自定义模型名。
*   model\_base\_path：所导出的模型文件所在目录。 这里推荐使用阿里云的EAS平台做深度学习模型部署，支持弹性部署和在线扩容。

# 在线预测

当使用TensorFlow Serving部署好了模型服务，下一步就可以跨语言的调用服务了。 Python调用：

```
from tensorflow_serving.apis import classification_pb2
from tensorflow_serving.apis import regression_pb2
from tensorflow_serving.apis import predict_pb2
from tensorflow_serving.apis import prediction_service_pb2

from grpc.beta import implementations

channel = implementations.insecure_channel(host, int(port))
stub = prediction_service_pb2.beta_create_PredictionService_stub(channel)

request = classification_pb2.ClassificationRequest()
example = request.input.example_list.examples.add()
example.features.feature['x'].float_list.value.extend(image[0].astype(float))

result = stub.Classify(request, 10.0)  # 10 secs timeout

```

Java调用： Java调用推荐使用阿里云的EAS平台，它提供了功能强大的调用服务的JavaSDK。







---


>https://www.cnblogs.com/xing901022/p/10216933.html

# 1 hello world篇

部署完docker后，如果是cpu环境，可以直接拉取tensorflow/serving，如果是GPU环境则麻烦点，具体参考前一篇，这里就不再赘述了。

cpu版本的可以直接拉取tensorflow/serving，docker会自动拉取latest版本：

```
docker pull tensorflow/serving

```

如果想要指定tensorflow的版本，可以去这里查看：[https://hub.docker.com/r/tensorflow/serving/tags/](https://hub.docker.com/r/tensorflow/serving/tags/)

比如我需要的是1.12.0版本的tf，那么也可以拉取指定的版本：

```
docker pull tensorflow/serving:1.12.0

```

拉取完镜像，需要下载一个hello world的程序代码。

```
mkdir -p /tmp/tfserving
cd /tmp/tfserving
git clone https://github.com/tensorflow/serving

```

tensorflow/serving的github中有对应的测试模型，模型其实就是 y = 0.5 \* x + 2。即输入一个数，输出是对应的y。

运行下面的命令，在docker中部署服务：

```
docker run -p 8501:8501 --mount type=bind,source=/tmp/serving/tensorflow_serving/servables/tensorflow/testdata/saved_model_half_plus_two_cpu,target=/models/half_plus_two -e MODEL_NAME=half_plus_two -t tensorflow/serving &

```

上面的命令中，把`/tmp/serving/tensorflow_serving/servables/tensorflow/testdata/saved_model_half_plus_two_cpu`路径挂载到`/models/half_plus_two`,这样tensorflow\_serving就可以加载models下的模型了，然后开放内部8501的http接口。

执行`docker ps`查看服务列表：

```
➜  ~ docker ps
CONTAINER ID        IMAGE                COMMAND                  CREATED             STATUS              PORTS                              NAMES
7decb4286057        tensorflow/serving   "/usr/bin/tf_serving…"   7 seconds ago       Up 6 seconds        8500/tcp, 0.0.0.0:8501->8501/tcp   eager_dewdney

```

发送一个http请求测试一下：

```
curl -d '{"instances": [1.0, 2.0, 5.0]}' -X POST http://localhost:8501/v1/models/half_plus_two:predict
{
    "predictions": [2.5, 3.0, 4.5
    ]
}%   

```

# 2 mnist篇

由于前面的例子，serving工程下只有pb模型，没有模型的训练和导出，因此看不出其中的门道。这一部分就直接基于手写体识别的例子，展示一下如何从tensorflow训练代码导出模型，又如何通过grpc服务进行模型的调用。

训练和导出:

```
#! /usr/bin/env python
"""
训练并导出Softmax回归模型，使用SaveModel导出训练模型并添加签名。
"""

from __future__ import print_function

import os
import sys

# This is a placeholder for a Google-internal import.

import tensorflow as tf
import ssl
ssl._create_default_https_context = ssl._create_unverified_context

import basic.mnist_input_data as mnist_input_data

# 定义模型参数
tf.app.flags.DEFINE_integer('training_iteration', 10, 'number of training iterations.')
tf.app.flags.DEFINE_integer('model_version', 2, 'version number of the model.')
tf.app.flags.DEFINE_string('work_dir', './tmp', 'Working directory.')
FLAGS = tf.app.flags.FLAGS

def main(_):

    # 参数校验
    # if len(sys.argv) < 2 or sys.argv[-1].startswith('-'):
    #     print('Usage: mnist_saved_model.py [--training_iteration=x] '
    #           '[--model_version=y] export_dir')
    #     sys.exit(-1)
    # if FLAGS.training_iteration <= 0:
    #     print('Please specify a positive value for training iteration.')
    #     sys.exit(-1)
    # if FLAGS.model_version <= 0:
    #     print('Please specify a positive value for version number.')
    #     sys.exit(-1)

    # Train model
    print('Training model...')

    mnist = mnist_input_data.read_data_sets(FLAGS.work_dir, one_hot=True)

    sess = tf.InteractiveSession()

    serialized_tf_example = tf.placeholder(tf.string, name='tf_example')
    feature_configs = {'x': tf.FixedLenFeature(shape=[784], dtype=tf.float32), }
    tf_example = tf.parse_example(serialized_tf_example, feature_configs)
    x = tf.identity(tf_example['x'], name='x')  # use tf.identity() to assign name
    y_ = tf.placeholder('float', shape=[None, 10])
    w = tf.Variable(tf.zeros([784, 10]))
    b = tf.Variable(tf.zeros([10]))
    sess.run(tf.global_variables_initializer())
    y = tf.nn.softmax(tf.matmul(x, w) + b, name='y')
    cross_entropy = -tf.reduce_sum(y_ * tf.log(y))
    train_step = tf.train.GradientDescentOptimizer(0.01).minimize(cross_entropy)
    values, indices = tf.nn.top_k(y, 10)
    table = tf.contrib.lookup.index_to_string_table_from_tensor(
        tf.constant([str(i) for i in range(10)]))
    prediction_classes = table.lookup(tf.to_int64(indices))
    for _ in range(FLAGS.training_iteration):
        batch = mnist.train.next_batch(50)
        train_step.run(feed_dict={x: batch[0], y_: batch[1]})
    correct_prediction = tf.equal(tf.argmax(y, 1), tf.argmax(y_, 1))
    accuracy = tf.reduce_mean(tf.cast(correct_prediction, 'float'))
    print('training accuracy %g' % sess.run(
        accuracy, feed_dict={
            x: mnist.test.images,
            y_: mnist.test.labels
        }))
    print('Done training!')

    # Export model
    # WARNING(break-tutorial-inline-code): The following code snippet is
    # in-lined in tutorials, please update tutorial documents accordingly
    # whenever code changes.

    # export_path_base = sys.argv[-1]
    export_path_base = "/Users/xingoo/PycharmProjects/ml-in-action/实践-tensorflow/01-官方文档-学习和使用ML/save_model"
    export_path = os.path.join(tf.compat.as_bytes(export_path_base), tf.compat.as_bytes(str(FLAGS.model_version)))
    print('Exporting trained model to', export_path)
    # 配置导出地址，创建SaveModel
    builder = tf.saved_model.builder.SavedModelBuilder(export_path)

    # Build the signature_def_map.

    # 创建TensorInfo，包含type,shape,name
    classification_inputs = tf.saved_model.utils.build_tensor_info(serialized_tf_example)
    classification_outputs_classes = tf.saved_model.utils.build_tensor_info(prediction_classes)
    classification_outputs_scores = tf.saved_model.utils.build_tensor_info(values)

    # 分类签名：算法类型+输入+输出（概率和名字）
    classification_signature = (
        tf.saved_model.signature_def_utils.build_signature_def(
            inputs={
                tf.saved_model.signature_constants.CLASSIFY_INPUTS:
                    classification_inputs
            },
            outputs={
                tf.saved_model.signature_constants.CLASSIFY_OUTPUT_CLASSES:
                    classification_outputs_classes,
                tf.saved_model.signature_constants.CLASSIFY_OUTPUT_SCORES:
                    classification_outputs_scores
            },
            method_name=tf.saved_model.signature_constants.CLASSIFY_METHOD_NAME))

    tensor_info_x = tf.saved_model.utils.build_tensor_info(x)
    tensor_info_y = tf.saved_model.utils.build_tensor_info(y)

    # 预测签名：输入的x和输出的y
    prediction_signature = (
        tf.saved_model.signature_def_utils.build_signature_def(
            inputs={'images': tensor_info_x},
            outputs={'scores': tensor_info_y},
            method_name=tf.saved_model.signature_constants.PREDICT_METHOD_NAME))

    # 构建图和变量的信息：
    """
    sess                会话
    tags                标签，默认提供serving、train、eval、gpu、tpu
    signature_def_map   签名
    main_op             初始化？
    strip_default_attrs strip?
    """
    # predict_images就是服务调用的方法
    # serving_default是没有输入签名时，使用的方法
    builder.add_meta_graph_and_variables(
        sess, [tf.saved_model.tag_constants.SERVING],
        signature_def_map={
            'predict_images':
                prediction_signature,
            tf.saved_model.signature_constants.DEFAULT_SERVING_SIGNATURE_DEF_KEY:
                classification_signature,
        },
        main_op=tf.tables_initializer(),
        strip_default_attrs=True)

    # 保存
    builder.save()

    print('Done exporting!')


if __name__ == '__main__':
    tf.app.run()

```

执行后，在当前目录中就有一个save\_model文件，保存了各个版本的pb模型文件。

然后基于grpc部署服务：

```
docker run -p 8500:8500 --mount type=bind,source=/Users/xingoo/PycharmProjects/ml-in-action/01-实践-tensorflow/01-官方文档-学习和使用ML/save_model,target=/models/mnist -e MODEL_NAME=mnist -t tensorflow/serving &

```

服务部署成功，查看一下docker列表：

```
➜  ~ docker ps
CONTAINER ID        IMAGE                COMMAND                  CREATED             STATUS              PORTS                              NAMES
39a06cc35961        tensorflow/serving   "/usr/bin/tf_serving…"   4 seconds ago       Up 3 seconds        0.0.0.0:8500->8500/tcp, 8501/tcp   hardcore_galileo

```

然后编写对应的client代码：

```
import tensorflow as tf
import basic.mnist_input_data as mnist_input_data
import grpc
import numpy as np
import sys
import threading

from tensorflow_serving.apis import predict_pb2
from tensorflow_serving.apis import prediction_service_pb2_grpc

tf.app.flags.DEFINE_integer('concurrency', 1, 'maximum number of concurrent inference requests')
tf.app.flags.DEFINE_integer('num_tests', 100, 'Number of test images')
tf.app.flags.DEFINE_string('server', 'localhost:8500', 'PredictionService host:port')
tf.app.flags.DEFINE_string('work_dir', './tmp', 'Working directory. ')
FLAGS = tf.app.flags.FLAGS

test_data_set = mnist_input_data.read_data_sets(FLAGS.work_dir).test
channel = grpc.insecure_channel(FLAGS.server)
stub = prediction_service_pb2_grpc.PredictionServiceStub(channel)

class _ResultCounter(object):
    """Counter for the prediction results."""

    def __init__(self, num_tests, concurrency):
        self._num_tests = num_tests
        self._concurrency = concurrency
        self._error = 0
        self._done = 0
        self._active = 0
        self._condition = threading.Condition()

    def inc_error(self):
        with self._condition:
            self._error += 1

    def inc_done(self):
        with self._condition:
            self._done += 1
            self._condition.notify()

    def dec_active(self):
        with self._condition:
            self._active -= 1
            self._condition.notify()

    def get_error_rate(self):
        with self._condition:
            while self._done != self._num_tests:
                self._condition.wait()
            return self._error / float(self._num_tests)

    def throttle(self):
        with self._condition:
            while self._active == self._concurrency:
                self._condition.wait()
            self._active += 1

def _create_rpc_callback(label, result_counter):
    def _callback(result_future):
        exception = result_future.exception()
        if exception:
            result_counter.inc_error()
            print(exception)
        else:
            response = np.array(result_future.result().outputs['scores'].float_val)
            prediction = np.argmax(response)
            sys.stdout.write("%s - %s\n" % (label, prediction))
            sys.stdout.flush()

        result_counter.inc_done()
        result_counter.dec_active()

    return _callback


result_counter = _ResultCounter(FLAGS.num_tests, FLAGS.concurrency)
for i in range(FLAGS.num_tests):
    request = predict_pb2.PredictRequest()
    request.model_spec.name = 'mnist'
    request.model_spec.signature_name = 'predict_images'
    image, label = test_data_set.next_batch(1)
    request.inputs['images'].CopyFrom(tf.contrib.util.make_tensor_proto(image[0], shape=[1, image[0].size]))
    result_counter.throttle()
    result_future = stub.Predict.future(request, 5.0)  # 5 seconds
    result_future.add_done_callback(_create_rpc_callback(label[0], result_counter))

print(result_counter.get_error_rate())

```

得到对应的输出：

```
3 - 3
6 - 6
9 - 9
3 - 3
1 - 1
4 - 9
1 - 5
7 - 9
6 - 6
9 - 9
0.0
```
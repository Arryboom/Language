# Is it possible to view RabbitMQ message contents directly from the command line?

>https://stackoverflow.com/questions/10709533/is-it-possible-to-view-rabbitmq-message-contents-directly-from-the-command-line

If you want multiple messages from a queue, say 10 messages, the command to use is:

```
rabbitmqadmin get queue=<QueueName> ackmode=ack_requeue_true count=10
```

If you don't want the messages requeued, just change `ackmode` to `ack_requeue_false`.
package generated;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.73.0)",
    comments = "Source: manager.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class ManagerGrpc {

  private ManagerGrpc() {}

  public static final java.lang.String SERVICE_NAME = "generated.Manager";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<generated.Task,
      generated.Empty> getDeployMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Deploy",
      requestType = generated.Task.class,
      responseType = generated.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<generated.Task,
      generated.Empty> getDeployMethod() {
    io.grpc.MethodDescriptor<generated.Task, generated.Empty> getDeployMethod;
    if ((getDeployMethod = ManagerGrpc.getDeployMethod) == null) {
      synchronized (ManagerGrpc.class) {
        if ((getDeployMethod = ManagerGrpc.getDeployMethod) == null) {
          ManagerGrpc.getDeployMethod = getDeployMethod =
              io.grpc.MethodDescriptor.<generated.Task, generated.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Deploy"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  generated.Task.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  generated.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new ManagerMethodDescriptorSupplier("Deploy"))
              .build();
        }
      }
    }
    return getDeployMethod;
  }

  private static volatile io.grpc.MethodDescriptor<generated.StringMessage,
      generated.Empty> getStopAllMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "StopAll",
      requestType = generated.StringMessage.class,
      responseType = generated.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<generated.StringMessage,
      generated.Empty> getStopAllMethod() {
    io.grpc.MethodDescriptor<generated.StringMessage, generated.Empty> getStopAllMethod;
    if ((getStopAllMethod = ManagerGrpc.getStopAllMethod) == null) {
      synchronized (ManagerGrpc.class) {
        if ((getStopAllMethod = ManagerGrpc.getStopAllMethod) == null) {
          ManagerGrpc.getStopAllMethod = getStopAllMethod =
              io.grpc.MethodDescriptor.<generated.StringMessage, generated.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "StopAll"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  generated.StringMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  generated.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new ManagerMethodDescriptorSupplier("StopAll"))
              .build();
        }
      }
    }
    return getStopAllMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ManagerStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ManagerStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ManagerStub>() {
        @java.lang.Override
        public ManagerStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ManagerStub(channel, callOptions);
        }
      };
    return ManagerStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static ManagerBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ManagerBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ManagerBlockingV2Stub>() {
        @java.lang.Override
        public ManagerBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ManagerBlockingV2Stub(channel, callOptions);
        }
      };
    return ManagerBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ManagerBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ManagerBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ManagerBlockingStub>() {
        @java.lang.Override
        public ManagerBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ManagerBlockingStub(channel, callOptions);
        }
      };
    return ManagerBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ManagerFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ManagerFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ManagerFutureStub>() {
        @java.lang.Override
        public ManagerFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ManagerFutureStub(channel, callOptions);
        }
      };
    return ManagerFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void deploy(generated.Task request,
        io.grpc.stub.StreamObserver<generated.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDeployMethod(), responseObserver);
    }

    /**
     */
    default void stopAll(generated.StringMessage request,
        io.grpc.stub.StreamObserver<generated.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getStopAllMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service Manager.
   */
  public static abstract class ManagerImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return ManagerGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service Manager.
   */
  public static final class ManagerStub
      extends io.grpc.stub.AbstractAsyncStub<ManagerStub> {
    private ManagerStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ManagerStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ManagerStub(channel, callOptions);
    }

    /**
     */
    public void deploy(generated.Task request,
        io.grpc.stub.StreamObserver<generated.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDeployMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void stopAll(generated.StringMessage request,
        io.grpc.stub.StreamObserver<generated.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getStopAllMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service Manager.
   */
  public static final class ManagerBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<ManagerBlockingV2Stub> {
    private ManagerBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ManagerBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ManagerBlockingV2Stub(channel, callOptions);
    }

    /**
     */
    public generated.Empty deploy(generated.Task request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeployMethod(), getCallOptions(), request);
    }

    /**
     */
    public generated.Empty stopAll(generated.StringMessage request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getStopAllMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service Manager.
   */
  public static final class ManagerBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<ManagerBlockingStub> {
    private ManagerBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ManagerBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ManagerBlockingStub(channel, callOptions);
    }

    /**
     */
    public generated.Empty deploy(generated.Task request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeployMethod(), getCallOptions(), request);
    }

    /**
     */
    public generated.Empty stopAll(generated.StringMessage request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getStopAllMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service Manager.
   */
  public static final class ManagerFutureStub
      extends io.grpc.stub.AbstractFutureStub<ManagerFutureStub> {
    private ManagerFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ManagerFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ManagerFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<generated.Empty> deploy(
        generated.Task request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDeployMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<generated.Empty> stopAll(
        generated.StringMessage request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getStopAllMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_DEPLOY = 0;
  private static final int METHODID_STOP_ALL = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_DEPLOY:
          serviceImpl.deploy((generated.Task) request,
              (io.grpc.stub.StreamObserver<generated.Empty>) responseObserver);
          break;
        case METHODID_STOP_ALL:
          serviceImpl.stopAll((generated.StringMessage) request,
              (io.grpc.stub.StreamObserver<generated.Empty>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getDeployMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              generated.Task,
              generated.Empty>(
                service, METHODID_DEPLOY)))
        .addMethod(
          getStopAllMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              generated.StringMessage,
              generated.Empty>(
                service, METHODID_STOP_ALL)))
        .build();
  }

  private static abstract class ManagerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ManagerBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return generated.ManagerOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Manager");
    }
  }

  private static final class ManagerFileDescriptorSupplier
      extends ManagerBaseDescriptorSupplier {
    ManagerFileDescriptorSupplier() {}
  }

  private static final class ManagerMethodDescriptorSupplier
      extends ManagerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    ManagerMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (ManagerGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ManagerFileDescriptorSupplier())
              .addMethod(getDeployMethod())
              .addMethod(getStopAllMethod())
              .build();
        }
      }
    }
    return result;
  }
}

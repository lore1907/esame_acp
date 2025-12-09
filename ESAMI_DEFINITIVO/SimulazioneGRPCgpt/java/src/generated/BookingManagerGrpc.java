package generated;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.73.0)",
    comments = "Source: protos/bookingService.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class BookingManagerGrpc {

  private BookingManagerGrpc() {}

  public static final java.lang.String SERVICE_NAME = "generated.BookingManager";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<generated.Empty,
      generated.StatsResponse> getGetTotalReservationsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetTotalReservations",
      requestType = generated.Empty.class,
      responseType = generated.StatsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<generated.Empty,
      generated.StatsResponse> getGetTotalReservationsMethod() {
    io.grpc.MethodDescriptor<generated.Empty, generated.StatsResponse> getGetTotalReservationsMethod;
    if ((getGetTotalReservationsMethod = BookingManagerGrpc.getGetTotalReservationsMethod) == null) {
      synchronized (BookingManagerGrpc.class) {
        if ((getGetTotalReservationsMethod = BookingManagerGrpc.getGetTotalReservationsMethod) == null) {
          BookingManagerGrpc.getGetTotalReservationsMethod = getGetTotalReservationsMethod =
              io.grpc.MethodDescriptor.<generated.Empty, generated.StatsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetTotalReservations"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  generated.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  generated.StatsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new BookingManagerMethodDescriptorSupplier("GetTotalReservations"))
              .build();
        }
      }
    }
    return getGetTotalReservationsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<generated.Empty,
      generated.Reservation> getGetLastReservationMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetLastReservation",
      requestType = generated.Empty.class,
      responseType = generated.Reservation.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<generated.Empty,
      generated.Reservation> getGetLastReservationMethod() {
    io.grpc.MethodDescriptor<generated.Empty, generated.Reservation> getGetLastReservationMethod;
    if ((getGetLastReservationMethod = BookingManagerGrpc.getGetLastReservationMethod) == null) {
      synchronized (BookingManagerGrpc.class) {
        if ((getGetLastReservationMethod = BookingManagerGrpc.getGetLastReservationMethod) == null) {
          BookingManagerGrpc.getGetLastReservationMethod = getGetLastReservationMethod =
              io.grpc.MethodDescriptor.<generated.Empty, generated.Reservation>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetLastReservation"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  generated.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  generated.Reservation.getDefaultInstance()))
              .setSchemaDescriptor(new BookingManagerMethodDescriptorSupplier("GetLastReservation"))
              .build();
        }
      }
    }
    return getGetLastReservationMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static BookingManagerStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BookingManagerStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BookingManagerStub>() {
        @java.lang.Override
        public BookingManagerStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BookingManagerStub(channel, callOptions);
        }
      };
    return BookingManagerStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static BookingManagerBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BookingManagerBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BookingManagerBlockingV2Stub>() {
        @java.lang.Override
        public BookingManagerBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BookingManagerBlockingV2Stub(channel, callOptions);
        }
      };
    return BookingManagerBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static BookingManagerBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BookingManagerBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BookingManagerBlockingStub>() {
        @java.lang.Override
        public BookingManagerBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BookingManagerBlockingStub(channel, callOptions);
        }
      };
    return BookingManagerBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static BookingManagerFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BookingManagerFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BookingManagerFutureStub>() {
        @java.lang.Override
        public BookingManagerFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BookingManagerFutureStub(channel, callOptions);
        }
      };
    return BookingManagerFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void getTotalReservations(generated.Empty request,
        io.grpc.stub.StreamObserver<generated.StatsResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetTotalReservationsMethod(), responseObserver);
    }

    /**
     */
    default void getLastReservation(generated.Empty request,
        io.grpc.stub.StreamObserver<generated.Reservation> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetLastReservationMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service BookingManager.
   */
  public static abstract class BookingManagerImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return BookingManagerGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service BookingManager.
   */
  public static final class BookingManagerStub
      extends io.grpc.stub.AbstractAsyncStub<BookingManagerStub> {
    private BookingManagerStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BookingManagerStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BookingManagerStub(channel, callOptions);
    }

    /**
     */
    public void getTotalReservations(generated.Empty request,
        io.grpc.stub.StreamObserver<generated.StatsResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetTotalReservationsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getLastReservation(generated.Empty request,
        io.grpc.stub.StreamObserver<generated.Reservation> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetLastReservationMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service BookingManager.
   */
  public static final class BookingManagerBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<BookingManagerBlockingV2Stub> {
    private BookingManagerBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BookingManagerBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BookingManagerBlockingV2Stub(channel, callOptions);
    }

    /**
     */
    public generated.StatsResponse getTotalReservations(generated.Empty request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetTotalReservationsMethod(), getCallOptions(), request);
    }

    /**
     */
    public generated.Reservation getLastReservation(generated.Empty request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetLastReservationMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service BookingManager.
   */
  public static final class BookingManagerBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<BookingManagerBlockingStub> {
    private BookingManagerBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BookingManagerBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BookingManagerBlockingStub(channel, callOptions);
    }

    /**
     */
    public generated.StatsResponse getTotalReservations(generated.Empty request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetTotalReservationsMethod(), getCallOptions(), request);
    }

    /**
     */
    public generated.Reservation getLastReservation(generated.Empty request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetLastReservationMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service BookingManager.
   */
  public static final class BookingManagerFutureStub
      extends io.grpc.stub.AbstractFutureStub<BookingManagerFutureStub> {
    private BookingManagerFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BookingManagerFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BookingManagerFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<generated.StatsResponse> getTotalReservations(
        generated.Empty request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetTotalReservationsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<generated.Reservation> getLastReservation(
        generated.Empty request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetLastReservationMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_TOTAL_RESERVATIONS = 0;
  private static final int METHODID_GET_LAST_RESERVATION = 1;

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
        case METHODID_GET_TOTAL_RESERVATIONS:
          serviceImpl.getTotalReservations((generated.Empty) request,
              (io.grpc.stub.StreamObserver<generated.StatsResponse>) responseObserver);
          break;
        case METHODID_GET_LAST_RESERVATION:
          serviceImpl.getLastReservation((generated.Empty) request,
              (io.grpc.stub.StreamObserver<generated.Reservation>) responseObserver);
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
          getGetTotalReservationsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              generated.Empty,
              generated.StatsResponse>(
                service, METHODID_GET_TOTAL_RESERVATIONS)))
        .addMethod(
          getGetLastReservationMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              generated.Empty,
              generated.Reservation>(
                service, METHODID_GET_LAST_RESERVATION)))
        .build();
  }

  private static abstract class BookingManagerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    BookingManagerBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return generated.BookingService.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("BookingManager");
    }
  }

  private static final class BookingManagerFileDescriptorSupplier
      extends BookingManagerBaseDescriptorSupplier {
    BookingManagerFileDescriptorSupplier() {}
  }

  private static final class BookingManagerMethodDescriptorSupplier
      extends BookingManagerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    BookingManagerMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (BookingManagerGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new BookingManagerFileDescriptorSupplier())
              .addMethod(getGetTotalReservationsMethod())
              .addMethod(getGetLastReservationMethod())
              .build();
        }
      }
    }
    return result;
  }
}

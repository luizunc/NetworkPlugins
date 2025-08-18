package minecraft.core.core.servers.balancer;

import minecraft.core.core.servers.balancer.elements.LoadBalancerObject;

public interface LoadBalancer<T extends LoadBalancerObject> {
  T next();
}

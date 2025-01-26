package pb.ajneb97.injector.services;

import pb.ajneb97.commons.loader.Loader;
import pb.ajneb97.commons.service.Service;
import team.unnamed.inject.Inject;

import java.util.Set;

public final class PluginService implements Service {

    @Inject
    private Set<Loader> loaderSet;
    @Inject
    private Set<Service> serviceSet;

    @Override
    public void start() {
        loaderSet.forEach(Loader::load);
        serviceSet.forEach(Service::start);
    }

    @Override
    public void finish() {
        serviceSet.forEach(Service::finish);
    }
}

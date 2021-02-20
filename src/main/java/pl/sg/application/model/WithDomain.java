package pl.sg.application.model;

public interface WithDomain<T extends WithDomain<T>> {
    Domain getDomain();
    T setDomain(Domain domain);
}

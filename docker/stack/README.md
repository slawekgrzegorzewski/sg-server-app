Docker registry API https://docker-docs.uclv.cu/registry/spec/api/

```bash
curl -X GET --basic -u <<user>>:<<password>> https://grzegorzewski.org:5000/v2/
```

```bash
curl -X GET --basic -u <<user>>:<<password>> https://grzegorzewski.org:5000/v2/backend/tags/list
```

```bash
curl -X GET --basic -u <<user>>:<<password>> --header "Accept: application/vnd.oci.image.manifest.v1+json" https://grzegorzewski.org:5000/v2/backend/manifests/764d427-debug-rpi4
```

```bash
curl -X DELETE --basic -u <<user>>:<<password>> https://grzegorzewski.org:5000/v2/backend/manifests/<reference>
```
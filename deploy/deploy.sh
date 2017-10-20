tag=$(git rev-parse HEAD)

## Deploy!
eb deploy --timeout 60 -l $tag;

#!/bin/sh

# Use gomplate to render the template file
if ! gomplate -f /etc/stunnel/stunnel.conf.tpl -o /etc/stunnel/stunnel.conf; then
  echo "gomplate command failed"
  exit -1
fi

# Execute the command specified in the CMD of the Dockerfile or the command passed to the docker run
exec "$@"
FROM alpine:3.20

RUN apk add --no-cache stunnel gomplate

COPY stunnel.conf.tpl /etc/stunnel/stunnel.conf.tpl

COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]
CMD ["stunnel", "/etc/stunnel/stunnel.conf"]
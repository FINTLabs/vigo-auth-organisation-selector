client = yes
foreground = yes

[ldap]
accept = 389
connect = {{ required "SERVER_ADDRESS environment variable is not defined" .Env.SERVER_ADDRESS }}:636
verify = 0
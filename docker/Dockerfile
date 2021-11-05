FROM cassandra:3.1.11

ENV CASSANDRA_KEYSPACE test_keyspace
ENV TABLE test_table

COPY entrypoint-wrap.sh /entrypoint-wrap.sh
ENTRYPOINT ["/entrypoint-wrap.sh"]
CMD ["cassandra", "-f"]
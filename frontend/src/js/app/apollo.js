import { ApolloClient } from 'apollo-client';
import { InMemoryCache } from 'apollo-cache-inmemory';
import { createHttpLink } from 'apollo-link-http';
import { setContext } from 'apollo-link-context';
import ls from 'store';

const httpLink = createHttpLink({
  uri: 'http://localhost:8081/graphql',
});

const middlewareLink = setContext(() => ({
  headers: {
    authorization: ls.get('jwt_token') || null,
  }
}));

const link = middlewareLink.concat(httpLink);

const client = new ApolloClient({
  link,
  cache: new InMemoryCache(),
  shouldBatch: true
});

export default client;

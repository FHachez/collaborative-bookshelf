package graphql.resolvers

class RootResolver(username: String) {

  def query = new QueryResolver(username)

  def mutation = new MutationResolver(username)

}
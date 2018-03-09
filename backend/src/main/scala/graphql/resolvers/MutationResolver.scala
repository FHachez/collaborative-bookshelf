package graphql.resolvers

class MutationResolver(username: String) {

  def book = new BookResolver()

}
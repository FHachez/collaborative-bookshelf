package graphql.resolvers

class RootResolver(username: String, companyID: Long) {

  def client = new ClientResolver(companyID)

}
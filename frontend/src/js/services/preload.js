import store from 'store';

export default function preload() {
  const jwtToken = store.get('jwt_token');

  if (jwtToken) {
    return {
      pages: {
        login: {
          store: {
            jwt: jwtToken,
          },
        },
      },
    };
  }

  return {};
}

import { CHANGE_SEARCH_TERM_VALUE } from './action-types';

export default function medicinesReducer(state = { searchTerm: '' }, action) {
  switch (action.type) {
    case CHANGE_SEARCH_TERM_VALUE: {
      return {
        ...state,
        searchTerm: action.payload,
      };
    }

    default:
      return state;
  }
}

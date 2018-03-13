import React, { Component } from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import debounce from 'lodash/debounce';
import { graphql, compose } from 'react-apollo';

import Search from 'grommet/components/Search';
import Box from 'grommet/components/Box';
import Button from 'grommet/components/Button';
import ListPlaceholder from 'grommet-addons/components/ListPlaceholder';
import AddIcon from 'grommet/components/icons/base/Add';

import { setSearchTerm } from './data/medicines-actions';

import SearchMedicines from './queries/searchMedicines.graphql';

import MedicinesList from './MedicinesList';

class Medicines extends Component {
  constructor() {
    super();

    this.state = {
      searchDebounce: debounce(this.handleSearchTermChange, 500)
    };
  }

  componentWillUnmount = () => {
    this.props.setSearchTerm('');
  };

  handleSearchTermChange = (event) => {
    event.preventDefault();

    const newSearchTerm = event.target.value;

    this.props.setSearchTerm(newSearchTerm);
  };

  renderMedicinesList = () => {
    const medicinesList = (() => {
      if (this.props.data.medicines) {
        return this.props.data.medicines;
      }
      return [];
    })();

    if (this.props.data && this.props.data.medicines && this.props.data.medicines.loading) {
      return <ListPlaceholder />;
    }

    return (
      medicinesList.length && medicinesList.length !== 0 ?
        <MedicinesList medicinesList={medicinesList} /> :
        <ListPlaceholder
          emptyMessage='You do not have any items at the moment.'
          unfilteredTotal={medicinesList.length}
          filteredTotal={medicinesList.length + 1}
        />
    );
  }

  render() {
    return (
      <Box>
        <Box
          pad='medium'
        >
          <Search
            placeHolder='Search for medicine'
            inline
            onDOMChange={this.state.searchDebounce}
          />
          {this.renderMedicinesList()}
        </Box>
        <Box pad='medium' align='end'>
          <Button>
            <AddIcon
              size='medium'
            />
          </Button>
        </Box>
      </Box>
    );
  }
}

const graphqlConnect = compose(
  graphql(SearchMedicines, {
    skip: props => !props.medicinesStore.searchTerm,
    options: props => ({
      variables: {
        searchTerm: props.medicinesStore.searchTerm,
      },
    }),
  }),
)(Medicines);

const mapStateToProps = state => (
  {
    medicinesStore: state.pages.medicines.store,
  }
);

export default connect(
  mapStateToProps,
  {
    setSearchTerm
  },
)(graphqlConnect);

Medicines.defaultProps = {
  data: {
    medicines: []
  }
};

Medicines.propTypes = {
  setSearchTerm: PropTypes.func.isRequired,

  // eslint-disable-next-line
  medicinesStore: PropTypes.shape({
    searchTerm: PropTypes.string
  }).isRequired,
  data: PropTypes.shape({
    medicines: PropTypes.array,
    loading: PropTypes.bool,
  })
};

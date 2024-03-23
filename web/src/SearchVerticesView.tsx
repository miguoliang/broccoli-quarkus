import { GetVertexParams, useGetVertex } from "./api.ts";

const SearchVerticesView = ({
                              q = "",
                              page = 0,
                              size = 10
                            }: Readonly<Partial<GetVertexParams>>) => {
  const { data } = useGetVertex({ q, page, size });
  return (
    <ul>
      {data?.data?.content?.map((vertex) => <li key={vertex.id}>{vertex.name}</li>)}
    </ul>
  );
};

export default SearchVerticesView;